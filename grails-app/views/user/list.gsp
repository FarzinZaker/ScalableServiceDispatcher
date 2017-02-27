<%--
  Created by IntelliJ IDEA.
  User: root
  Date: 8/14/14
  Time: 4:48 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title><g:message code="menu.users.${role}"/></title>
</head>

<body>
<div class="container">
    <div class="row">
        <div class="col-xs-12">
            <layout:breadcrumb items="${[
                    [text: '', url: createLink(uri: '/')],
                    [text: message(code: 'menu.users'), url: createLink(controller: 'user')],
                    [text: message(code: "menu.users.${role}"), url: createLink(controller: 'user', action: 'list')]
            ]}"/>
        </div>
    </div>

    <div class="row">
        <div class="col-xs-12">

            <div class="k-rtl">
                <div id="grid"></div>
            </div>

            <script>
                $(document).ready(function () {
                    $("#grid").kendoGrid({

                        dataSource: {
                            transport: {
                                type: 'odata',
                                read: {
                                    url: "${createLink(action: 'jsonList', id: role)}",
                                    dataType: "json",
                                    type: "POST"

                                },
                                parameterMap: function (data, action) {
                                    if (action === "read") {
//                                        data.search = $('#search').val();
                                        return data;
                                    } else {
                                        return data;
                                    }
                                }
                            },
                            schema: {
                                model: {
                                    fields: {
                                        id: {type: "number"},
                                        firstName: {type: "string"},
                                        lastName: {type: "string"},
                                        username: {type: "string"},
                                        sex: {type: "string"},
                                        mobile: {type: "string"},
                                        enabled: {type: "boolean"}
                                    }
                                },
                                data: "data", // records are returned in the "data" field of the response
                                total: "total"
                            },
                            pageSize: 20,
                            serverPaging: true,
                            serverFiltering: true,
                            serverSorting: true
                        },
//                        height: 550,
                        filterable: false,
                        sortable: true,
                        pageable: true,
                        columns: [
                            {
                                field: "id",
                                title: "${message(code:'user.id.label')}",
                                width: "100px",
                                attributes: {style: "text-align: center"},
                                headerAttributes: {style: "text-align: center"}
                            },
                            {
                                field: "firstName",
                                title: "${message(code:'user.firstName.label')}"
                            },
                            {
                                field: "lastName",
                                title: "${message(code:'user.lastName.label')}"
                            },
                            {
                                field: "username",
                                title: "${message(code:'user.email.label')}"
                            },
                            {
                                field: "sex",
                                title: "${message(code:'user.sex.label')}",
                                width: "80px"
                            },
                            {
                                field: "mobile",
                                title: "${message(code:'user.mobile.label')}",
                                width: "100px"
                            },
                            {
                                field: "enabled",
                                title: "${message(code:'user.enabled.label')}",
                                template: "<i class=\"fa #: enabled ? 'fa-thumbs-up' : '' #\"></i>",
                                width: "32px",
                                attributes: {style: "text-align: center"},
                                headerAttributes: {style: "text-align: center"}
                            },
                            {
                                command: {text: "${message(code:'edit')}", click: editGridItem},
                                title: "",
                                width: "85px",
                                headerAttributes: {style: "text-align: center"}
                            }
                        ],
                        toolbar: [
                            {
                                template: "<a class='k-button k-button-icontext k-grid-add' href='javascript:addGridItem();'>${message(code: 'user.add')}</a>"
                                %{--+ "<div class='searchbox k-textbox' id='search_container'>"--}%
                                %{--+ "<input data-role='searchbox' name='search' id='search' placeholder='${message(code:'search')}' type='text'>"--}%
                                %{--+ "<a class='k-icon k-i-search k-search'></a>"--}%
                                %{--+ "</div>"--}%
                            }
                        ]
                    });

//                    $("#search_container input").keypress(function (e) {
//                        if (e.keyCode == 13) {
//                            searchList();
//                            return false;
//                        }
//                    });
//                    $("#search_container a").click(function () {
//                        searchList();
//                        return false;
//                    });
                });

                function addGridItem() {
                    window.location.href = "${createLink(action: 'create', params: [role:role])}"
                }

                function editGridItem(e) {
                    window.location.href = "${createLink(action: 'edit')}/" + this.dataItem($(e.currentTarget).closest("tr")).id + "?role=${role}"
                }

                function searchList() {
                    var queryListView = $('#grid').data('kendoGrid');
                    queryListView.dataSource.read();   // added line
                    queryListView.dataSource.page(1);
                    queryListView.refresh();
                }
            </script>
        </div>
    </div>
</div>

<div class="hidden k-rtl">
    <div id="importWindow"></div>
</div>
</body>
</html>