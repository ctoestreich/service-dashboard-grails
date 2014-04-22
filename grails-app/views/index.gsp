<%@ page import="com.bsb.Settings; com.bsb.EnvironmentType; com.bsb.ServiceType" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
</head>

<body>
<div ng-controller="ServiceEndpointCtrl">
<div class="row col-sm-12">
    <form id="myForm" class="form-inline form-search">
        <div class="row">
            <div class="col-sm-2">
                <label data-toggle="tooltip" title="Click to sort"
                       ng-click="setOrderByField('endpointName')">Name&nbsp;<i class="icon-question-sign"></i>
                </label><BR>
                <input type="text" ng-model="search.endpointName" class="input-sm form-control"
                       placeholder="Service Name">
            </div>

            <div class="col-sm-2">
                <label data-toggle="tooltip" title="Click to sort"
                       ng-click="setOrderByField('environmentType.name')">Environment&nbsp;<i
                        class="icon-question-sign"></i></label><BR>
                <select name="search.environmentType" id="search.environmentType"
                        class="form-control input-sm" ng-model="search.environmentType"><option
                        value="">Environment Filter...</option>
                    <option ng-repeat="e in environmentTypes" value="{{e.name}}"
                            ng-selected="search.environmentType == e.name">{{e.name}}</option>
                </select>
            </div>

            <div class="col-sm-2">
                <label data-toggle="tooltip" title="Click to sort"
                       ng-click="setOrderByField('serviceType.name')">Protocol&nbsp;<i class="icon-question-sign"></i>
                </label><BR>
                <select name="search.serviceType" id="search.serviceType"
                        class="form-control input-sm" ng-model="search.serviceType"><option
                        value="">Type Filter...</option>
                    <option ng-repeat="e in serviceTypes" value="{{e.name}}"
                            ng-selected="search.serviceType == e.name">{{e.name}}</option>
                </select>
            </div>

            <div class="col-sm-6">
                <label>&nbsp;</label><BR>

                <div class="col-sm-12">

                    <a ng-click="setDefaultState()" class="btn btn-primary btn-sm" data-toggle="tooltip"
                       title="Set current filter params to cookied default">Make Default</a>


                    %{--<label>&nbsp;</label><BR>--}%
                    <a data-toggle="tooltip" title="Refresh current view" ng-click="refreshAll()"
                       class="btn btn-purple btn-sm"><i
                            class="icon-refresh"></i>&nbsp;Refresh All</a>


                    %{--<label>&nbsp;</label><BR>--}%
                    <a class="btn btn-info btn-sm" ng-click="createEndpoint()" data-toggle="modal"
                       href="#editModal">
                        <i class="icon-plus-sign"></i>&nbsp;Add Monitor</a>

                    <a class="btn btn-primary btn-sm" data-toggle="modal" href="#settingsModal">
                        <i class="icon-gears"></i>&nbsp;Settings</a>
                </div>

            </div>
        </div>
    </form>
</div>
<br/>

<div class="widgets-container">
<span ng-repeat="endpoint in filteredEndpoints = (endpoints | filter:search) | orderBy:orderByField:reverse">
    <service-endpoint endpoint="endpoint"
                      delete="deleteEndpoint"
                      edit="editEndpoint"
                      view="viewEndpoint"
                      status="viewEndpointResponse"
                      copy="copyEndpoint"
                      error="viewError"
    />
</span>

<div class="modal fade" id="settingsModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">Global Service Settings</h4>
            </div>

            <div class="modal-body">
                <form class="form-horizontal" role="form" id="globalSettingsForm">
                    <div class="form-group">
                        <label for="connectionTimeout" class="col-sm-4 control-label">Connection Timeout (ms)</label>

                        <div class="col-sm-8">
                            <input name="connectionTimeout" ng-model="settings.connectionTimeout"
                                   class="form-control" id="connectionTimeout" placeholder="30000">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="readTimeout" class="col-sm-4 control-label">Read Timeout (ms)</label>

                        <div class="col-sm-8">
                            <input name="readTimeout" ng-model="settings.readTimeout"
                                   class="form-control" id="readTimeout" placeholder="30000">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="refreshTimer" class="col-sm-4 control-label">Refresh Interval</label>

                        <div class="col-sm-8">
                            <input name="refreshTimer" ng-model="settings.refreshTimer"
                                   class="form-control" id="refreshTimer" placeholder="120000">
                            <p>This is the interval that the server actually polls all services via quartz.  Changing this will cancel and reschedule the quartz job with the new interval.</p>
                        </div>
                    </div>
                </form>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-warning" data-dismiss="modal"
                        ng-click="resetSettings()">Cancel</button>
                <button type="button" class="btn btn-success" data-dismiss="modal"
                        ng-click="saveSettings()">Save</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>

<div class="modal fade" id="viewModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">Service Endpoint</h4>
            </div>

            <div class="modal-body">
                <form class="form-horizontal" role="form" id="viewServiceEndpointForm">
                    <div class="form-group">
                        <label for="serviceName" class="col-sm-3 control-label">Endpoint Name</label>

                        <div class="col-sm-9">{{endpointToView.endpointName}}</div>
                    </div>

                    <div class="form-group">
                        <label for="serviceType" class="col-sm-3 control-label">Request Type</label>

                        <div class="col-sm-9">{{endpointToView.serviceType.name}}</div>
                    </div>

                    <div class="form-group">
                        <label for="environmentType" class="col-sm-3 control-label">Environment Type</label>

                        <div class="col-sm-9">{{endpointToView.environmentType.name}}</div>
                    </div>

                    <div class="form-group">
                        <label for="requestUrl" class="col-sm-3 control-label">Request Url</label>

                        <div class="col-sm-9">{{endpointToView.requestUrl}}</div>
                    </div>

                    <div class="form-group" ng-show="endpointToView.serviceType.name == 'SOAP'">
                        <label for="soapAction" class="col-sm-3 control-label">Soap Action</label>

                        <div class="col-sm-9">{{endpointToView.soapAction}}</div>
                    </div>

                    <div class="form-group" ng-show="endpointToEdit.requestBody">
                        <label for="requestBody" class="col-sm-3 control-label">Request Body</label>

                        <div class="col-sm-9">{{endpointToEdit.requestBody}}</div>
                    </div>

                    <div class="form-group">
                        <label for="successNode"
                               data-toggle="tooltip"
                               title="XML child node (bar) or JSON path (foo.bar)"
                               class="col-sm-3 control-label">Success Node&nbsp;<i class="icon-question-sign"></i>
                        </label>

                        <div class="col-sm-9">{{endpointToView.successNode}}</div>
                    </div>

                    <div class="form-group">
                        <label data-toggle="tooltip"
                               title="String or regex (with NO wrapping slashes) value to compare against xml or json node"
                               for="successValue" class="col-sm-3 control-label">Success Value&nbsp;<i
                                class="icon-question-sign"></i></label>

                        <div class="col-sm-9">{{endpointToView.successValue}}</div>
                    </div>

                    <div class="form-group">
                        <label for="pollIntervalMilliSeconds"
                               class="col-sm-3 control-label">Poll Interval (ms)</label>

                        <div class="col-sm-9">{{endpointToView.pollIntervalMilliSeconds}}</div>
                    </div>
                </form>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-warning" data-dismiss="modal">Close</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>

<div class="modal wide fade" id="statusModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">Service Endpoint Last Response</h4>
            </div>

            <div class="modal-body">
                <pre>{{formatData(endpointStatus.lastResponse)}}</pre>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-warning" data-dismiss="modal">Close</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>

<div class="modal fade" id="editModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">Service Endpoint</h4>
            </div>

            <div class="modal-body">
                <form class="form-horizontal" role="form" id="newServiceEndpointForm">
                    <input type="hidden" name="id" ng-model="endpointToEdit.id"/>

                    <div class="form-group">
                        <label for="serviceName" class="col-sm-3 control-label">Endpoint Name</label>

                        <div class="col-sm-9">
                            <input name="endpointName" ng-model="endpointToEdit.endpointName"
                                   class="form-control" id="serviceName" placeholder="Service Name">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="serviceType" class="col-sm-3 control-label">Request Type</label>

                        <div class="col-sm-9">
                            <select name="serviceType" id="serviceType" class="form-control"
                                    ng-model="endpointToEdit.serviceType.name"
                                    ng-options="e.name as e.name for e in serviceTypes"
                                    ng-change="updateServiceType()">
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="environmentType" class="col-sm-3 control-label">Environment Type</label>

                        <div class="col-sm-9">
                            <select name="environmentType" id="environmentType" class="form-control"
                                    ng-model="endpointToEdit.environmentType.name"
                                    ng-options="e.name as e.name for e in environmentTypes"
                                    ng-change="updateEnvironmentType()"></select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="requestUrl" class="col-sm-3 control-label">Request Url</label>

                        <div class="col-sm-9">
                            <input name="requestUrl" ng-model="endpointToEdit.requestUrl" class="form-control"
                                   id="requestUrl" placeholder="Url (Include query string params for REST)">
                        </div>
                    </div>

                    <div class="form-group" ng-show="endpointToEdit.serviceType.name == 'SOAP'">
                        <label for="soapAction" class="col-sm-3 control-label">Soap Action</label>

                        <div class="col-sm-9">
                            <input name="soapAction" ng-model="endpointToEdit.soapAction" class="form-control"
                                   id="soapAction" placeholder="Soap Action (http://Foo.Service/Foo)">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="requestBody" class="col-sm-3 control-label">Request Body</label>

                        <div class="col-sm-9">
                            <textarea rows="8" name="requestBody" id="requestBody"
                                      class="form-control col-sm-12" placeholder="Data To Post (SOAP UI Request Body)"
                                      ng-model="endpointToEdit.requestBody">
                                {{endpointToEdit.requestBody}}
                            </textarea>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="successNode"
                               data-toggle="tooltip"
                               title="XML child node (bar) or JSON path (foo.bar)"
                               class="col-sm-3 control-label">Success Node&nbsp;<i class="icon-question-sign"></i>
                        </label>

                        <div class="col-sm-9">
                            <input name="successNode" ng-model="endpointToEdit.successNode"
                                   class="form-control" id="successNode" placeholder="XML Node Name/JSON Key">
                        </div>
                    </div>

                    <div class="form-group">
                        <label data-toggle="tooltip"
                               title="String or regex (with NO wrapping slashes) value to compare against xml or json node"
                               for="successValue" class="col-sm-3 control-label">Success Value&nbsp;<i
                                class="icon-question-sign"></i></label>

                        <div class="col-sm-9">
                            <input name="successValue" ng-model="endpointToEdit.successValue"
                                   class="form-control" id="successValue"
                                   placeholder="XML Node Value/JSON Value/Regex Supported">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="pollIntervalMilliSeconds"
                               class="col-sm-3 control-label">Poll Interval (ms)</label>

                        <div class="col-sm-9">
                            <input name="pollIntervalMilliSeconds"
                                   ng-model="endpointToEdit.pollIntervalMilliSeconds" class="form-control"
                                   id="pollIntervalMilliSeconds" placeholder="30000">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="pollIntervalMilliSeconds"
                               class="col-sm-3 control-label">&nbsp;</label>

                        <div class="col-sm-9">
                            Polling interval only affected the time at which the UI queries the server to check for last polled status.  The server will
                            automatically update the status of the service every ${Settings.globalRefreshTimer}ms OR when you click on the refresh button in the
                            service widget.  You may change the global refresh timer under the settings.</div>
                    </div>
                </form>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-warning" data-dismiss="modal">Cancel</button>
                <button type="button" class="btn btn-success" data-dismiss="modal"
                        ng-click="confirmSave()">Save</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="deleteModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">Confirm Delete</h4>
            </div>

            <div class="modal-body">
                {{ endpointToDelete.endpointName }}<br>
                {{ endpointToDelete.endpointUrl }}<br>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                <button type="button" class="btn btn-danger" data-dismiss="modal"
                        ng-click="confirmDelete(endpointToDelete.id)">Confirm Delete</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>

<div class="modal fade" id="errorModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">Error Details</h4>
            </div>

            <div class="modal-body">
                <span ng-bind-html="error"></span>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>

</div>

</div>
</body>
</html>
