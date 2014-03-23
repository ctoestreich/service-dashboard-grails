package com.bsb

import wslite.rest.RESTClient
import wslite.rest.RESTClientException
import wslite.soap.SOAPClient
import wslite.soap.SOAPVersion

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

            def success = (node?.toString()?.contains(serviceEndpoint.successValue) || node?.toString() ==~ serviceEndpoint.successValue)
            result.up = response.statusCode == 200
            result.down = response.statusCode != 200
            result.success = success
            if (!success) {
                result.exception = "Using Node: ${serviceEndpoint.successNode} and found: ${node}<br/>Looking for value: ${serviceEndpoint.successValue}<BR>Was: ${success}"
            }
            result.lastResponse = response?.json ?: (response ?: "") as Serializable

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
            def success = response.body.depthFirst().findAll {
                it.name() == serviceEndpoint.successNode && (it.text().equalsIgnoreCase(serviceEndpoint.successValue) || it.text() ==~ serviceEndpoint.successValue)
            }
            def node = response.body.depthFirst().findAll {
                it.name() == serviceEndpoint.successNode
            }

            result.lastResponse = (response?.text ?: '')
            result.up = response.httpResponse.statusCode == 200
            result.down = response.httpResponse.statusCode != 200
            result.success = success.size() > 0
            if (!success) {
                result.exception = "Using Node: ${serviceEndpoint.successNode} and found: ${node*.toString()}<br/>Looking for value: ${serviceEndpoint.successValue}"
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
