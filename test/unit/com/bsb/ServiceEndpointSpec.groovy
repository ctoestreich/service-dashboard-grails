package com.bsb

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification
/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
class ServiceEndpointSpec extends Specification {

    void 'Test validation'() {
        given:
        mockForConstraintsTests ServiceEndpoint

        when: 'the author name has only 4 characters'
        def serviceEndpoint = new ServiceEndpoint(endpointName: '',
                requestUrl: "https://test2as.bluestembrands.com/Utility.svc",
                soapAction: 'http://Fingerhut.Services/IUtilitySystem/TokenizeBankAccount',
                requestBody: """<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"> <soapenv:Body> <ns1:TokenizeBankAccount xmlns:ns1="http://Fingerhut.Services"> <ns1:BankAccountNumber>999000111</ns1:BankAccountNumber> </ns1:TokenizeBankAccount> </soapenv:Body> </soapenv:Envelope>""",
                successNode: 'ErrorMessage',
                successValue: 'SUCCESS',
                pollIntervalMilliSeconds: 64000,
                environmentType: EnvironmentType.TEST
        )

        then: 'validation should fail'
        !serviceEndpoint.validate()
        serviceEndpoint.hasErrors()
        serviceEndpoint.errors['endpointName'] == 'nullable'

        when: 'the author name has 5 characters'
        serviceEndpoint.endpointName = 'TokenizeBankAccount'

        then: 'validation should pass'
        serviceEndpoint.validate()
        !serviceEndpoint.hasErrors()
    }
}
