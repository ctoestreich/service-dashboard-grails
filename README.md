Soap &amp; Rest Service Dashboard
=================

This is a utility to ping and quickly identify the status of SOAP and REST endpoints.  This project uses [Grails](http://grails.org/), [AngularJS](http://angularjs.org/) and [Bootstrap](http://getbootstrap.com/)
to accomplish this easily and with style.

The project comes wired with a few examples of SOAP and REST endpoints.  One of these is intentionally set to fail to show a failure view.

SOAP
-------
When creating a soap endpoint, the thing to note is the Soap Action.  This is sometimes required to test a service.  I have found that it is easier to check in SoapUI on the WS-A tab in the Action field to get this value if you are unsure of what it is supposed to be.

The success node uses an xml node finder so only the specific node you are looking for is needed.  If you want to test Foo below, simply use Foo in the Success Node field

```
<xml>
<Parent>
	<Child>
		<Foo>Bar</Foo>
	</Child>
</Parent>
```

REST
-------
Rest endpoints are pretty simple to setup.  The success node will be the complete json dot path for the value.  Given the response below you would use foo.bar if you wanted to test the bar value.  The test

```
foo {
	bar: 'hello'
}
```

SUCCESS VALUE
-------
The Success value can be either a value literal like "FOO" or can be a regex like `/FOO/`


