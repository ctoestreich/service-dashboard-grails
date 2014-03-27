package com.bsb

import wslite.rest.RESTClient
import wslite.rest.RESTClientException
import wslite.soap.SOAPClient
import wslite.soap.SOAPVersion

import java.util.regex.Pattern

class PollingService {

    static transactional = false

    ServiceEndpointStatus getGetServiceEndpointStatus(ServiceEndpoint serviceEndpoint) {
        LinkedHashMap<String, Object> result = [up: true, down: false, success: false, exception: "", lastResponse: ""]
        if (serviceEndpoint?.serviceType == ServiceType.SOAP) {
            result = result + pingSoapService(serviceEndpoint)
        } else if (serviceEndpoint?.serviceType == ServiceType.REST) {
            result = result + pingRestService(serviceEndpoint)
        }

        ServiceEndpointStatus serviceEndpointStatus = serviceEndpoint?.serviceEndpointStatus

        if(!serviceEndpointStatus){
            result = result + [serviceEndpoint: serviceEndpoint]
            serviceEndpointStatus = new ServiceEndpointStatus()
        }

        serviceEndpointStatus.properties = result

        serviceEndpointStatus
    }

    private static LinkedHashMap<String, Serializable> pingRestService(ServiceEndpoint serviceEndpoint) {
        RESTClient restClient = new RESTClient(serviceEndpoint.requestUrl)
        restClient.httpClient.sslTrustAllCerts = true
        LinkedHashMap<String, Serializable> result = []

        try {
            def response = restClient.get(
                    followRedirects: false,
                    useCaches: false,
                    sslTrustAllCerts: true,
                    connectTimeout: Settings.getGlobalConnectionTimeout(),
                    readTimeout: Settings.getGlobalReadTimeout())
            def node = response.json
            serviceEndpoint.successNode.split(/\./).each {
                node = node."${it}"
            }

            Pattern pattern = Pattern.compile(serviceEndpoint.successValue)

            def success = (node?.toString()?.contains(serviceEndpoint.successValue) || pattern.matcher(node?.toString() ?: "").matches())
            result.up = response.statusCode == 200
            result.down = response.statusCode != 200
            result.success = success
            if (!success) {
                result.exception = "Using Node: ${serviceEndpoint.successNode} and found: ${node?.toString()}<br/>Looking for value: ${serviceEndpoint.successValue}"
            }
            result.lastResponse = response?.json?.toString() ?: ""

        } catch (RESTClientException e) {
            result.up = false
            result.success = false
            result.down = true
            result.exception = e.cause.toString()
        }
        result
    }

    private static LinkedHashMap<String, Serializable> pingSoapService(ServiceEndpoint serviceEndpoint) {
        SOAPClient soapClient = new SOAPClient(serviceEndpoint.requestUrl)
        soapClient.httpClient.sslTrustAllCerts = true
        def requestXML = serviceEndpoint.requestBody
        LinkedHashMap<String, Serializable> result = []

        try {
            def response = soapClient.send(
                    SOAPAction: serviceEndpoint.soapAction,
                    SOAPVersion.V1_1,
                    connectTimeout: Settings.getGlobalConnectionTimeout(),
                    readTimeout: Settings.getGlobalReadTimeout(),
                    requestXML)

            def node
            def value = []

            def success = response.body.depthFirst().findAll {
                Pattern pattern = Pattern.compile(serviceEndpoint.successValue)
                def matches = false
                if(it.name() == serviceEndpoint.successNode){
                    node = it.name()
                    value << it.text()
                    matches = (it?.text().equalsIgnoreCase(serviceEndpoint.successValue) || pattern?.matcher(it?.text())?.matches())
                }
                matches
            }

            result.lastResponse = (response?.text ?: '')
            result.up = response.httpResponse.statusCode == 200
            result.down = response.httpResponse.statusCode != 200
            result.success = success.size() > 0
            if (!success) {
                result.exception = "Using Node: ${serviceEndpoint.successNode} and found: ${value.join(', ')}<br/>Looking for value: ${serviceEndpoint.successValue}"
            }
        } catch (Exception e) {
            result.up = false
            result.down = true
            result.success = false
            result.exception = e.message
        }

        result
    }
}
