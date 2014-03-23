import com.bsb.EnvironmentType
import com.bsb.ServiceEndpoint
import com.bsb.ServiceType
import com.bsb.Settings

class BootStrap {

    def schedulingService

    def init = { servletContext ->
        wireTestData()
        //start manually due to db configured interval
        schedulingService.schedulePolling(Settings.globalRefreshTimer)
    }

    def wireTestData() {

        if (Settings.list().size() == 0) {
            new Settings().save()
        }

        new ServiceEndpoint(
                endpointName: 'Address Standardization',
                serviceType: ServiceType.SOAP,
                environmentType: EnvironmentType.PROD,
                requestUrl: 'http://www.webservicex.net/CurrencyConvertor.asmx',
                requestBody: """<soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope" xmlns:web="http://www.webserviceX.NET/">
   <soap:Body>
      <web:ConversionRate>
         <web:FromCurrency>USD</web:FromCurrency>
         <web:ToCurrency>CAD</web:ToCurrency>
      </web:ConversionRate>
   </soap:Body>
</soap:Envelope>""",
                successNode: 'ConversionRateResult',
                successValue: /\d+\.*\d*/
        ).save()

        new ServiceEndpoint(
                endpointName: 'Address Standardization Failure',
                serviceType: ServiceType.SOAP,
                environmentType: EnvironmentType.TEST,
                requestUrl: 'http://www.webservicex.net/CurrencyConvertor.asmx',
                requestBody: """<soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope" xmlns:web="http://www.webserviceX.NET/">
   <soap:Body>
      <web:ConversionRate>
         <web:FromCurrency>USD</web:FromCurrency>
         <web:ToCurrency>CAD</web:ToCurrency>
      </web:ConversionRate>
   </soap:Body>
</soap:Envelope>""",
                successNode: 'ConversionRateResult',
                successValue: 'This Will Error'
        ).save()

        new ServiceEndpoint(
                endpointName: 'Github User Account',
                serviceType: ServiceType.REST,
                requestUrl: 'https://api.github.com/users/grails-plugin-consortium',
                environmentType: EnvironmentType.STAGE,
                successNode: 'login',
                successValue: 'Grails-Plugin-Consortium'
        ).save()

        new ServiceEndpoint(
                endpointName: 'Github User Repos',
                serviceType: ServiceType.REST,
                requestUrl: 'https://api.github.com/users/Grails-Plugin-Consortium/repos',
                environmentType: EnvironmentType.DEV,
                successNode: 'owner.login',
                successValue: 'Grails-Plugin-Consortium'
        ).save()

    }

}
