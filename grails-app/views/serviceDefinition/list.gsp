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
    <title><g:message code="menu.services.serviceDefinition"/></title>
</head>

<body>
<div class="container">
    <div class="row">
        <div class="col-xs-12">
            <layout:breadcrumb items="${[
                    [text: '', url: createLink(uri: '/')],
                    [text: message(code: 'menu.services'), url: '#'],
                    [text: message(code: 'menu.services.serviceDefinition'), url: createLink(action: 'list')]
            ]}"/>
        </div>
    </div>

    <div class="row">
        <div class="col-xs-12">

            <div class="k-rtl">
                <div id="grid"></div>
            </div>
        </div>
    </div>
</div>

<div class="hidden k-rtl">
    <div id="formWindow"></div>
</div>

<div class="hidden" id="formWindowLoading">
    <div>
        <span class="loading" style="display: block">
            <img src="${resource(dir: 'images', file: 'loading.gif')}"/>
            <span><g:message code="wait"/></span>
        </span>
    </div>
</div>


<script>
    $(document).ready(function () {
        $("#grid").kendoGrid({

            dataSource: {
                transport: {
                    type: 'odata',
                    read: {
                        url: "${createLink(action: 'jsonList')}",
                        dataType: "json",
                        type: "POST"

                    },
                    parameterMap: function (data, action) {
                        if (action === "read") {
                            data.parent = $('#parent').val();
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
                            name: {type: "string"},
                            englishName: {type: "string"},
                            lastUpdated: {type: "string"}
                        }
                    },
                    data: "data",
                    total: "total"
                },
                pageSize: 10,
                serverPaging: true,
                serverFiltering: true,
                serverSorting: true,
                sort: {field: "name", dir: "asc"}
            },
            filterable: false,
            sortable: true,
            pageable: true,
            groupable: false,
            columns: [
                {
                    field: "id",
                    title: "${message(code:'serviceDefinition.id.label')}",
                    filterable: false
                },
                {
                    field: "name",
                    title: "${message(code:'serviceDefinition.name.label')}",
                    filterable: false
                },
                {
                    field: "englishName",
                    title: "${message(code:'serviceDefinition.englishName.label')}",
                    filterable: false
                },
                {
                    field: "isBulk",
                    title: "${message(code:'serviceDefinition.isBulk.label')}",
                    filterable: false,
                    template: "<i class=\"fa #: isBulk ? 'fa-thumbs-up' : '' #\"></i>",
                    attributes: {style: "text-align: center"},
                    headerAttributes: {style: "text-align: center"}
                },
                {
                    field: "lastUpdated",
                    title: "${message(code:'serviceDefinition.lastUpdated.label')}",
                    filterable: false
                },
                {
                    command: {text: "${message(code:'serviceDefinition.parameters.list')}", click: parameterList},
                    title: "",
                    width: "90px",
                    headerAttributes: {style: "text-align: center"}
                },
                {
                    command: {text: "${message(code:'serviceDefinition.instance.list')}", click: instanceList},
                    title: "",
                    width: "90px",
                    headerAttributes: {style: "text-align: center"}
                },
                {
                    command: {text: "${message(code:'edit')}", click: editGridItem},
                    title: "",
                    width: "85px",
                    headerAttributes: {style: "text-align: center"}
                },
                {
                    command: {text: "${message(code:'remove')}", click: removeGridItem},
                    title: "",
                    width: "85px",
                    headerAttributes: {style: "text-align: center"}
                }
            ],
            toolbar: [
                {
                    template: "<a class='k-button k-button-icontext k-grid-add' href='javascript:addGridItem();'>${message(code: 'serviceDefinition.add')}</a>"
                }
            ]
        });
    });

    function addGridItem() {
        $('#formWindow').html($('#formWindowLoading').html())
            .kendoWindow({
                width: '820px',
                content: '${createLink(action: 'create')}',
                modal: true,
                close: function (e) {
                }
            }).data('kendoWindow').title('${message(code:'serviceDefinition.add')}').center().open().bind("refresh", function () {
            $('#formWindow').data('kendoWindow').center().open();
        });
    }

    function editGridItem(e) {
        $('#formWindow').html($('#formWindowLoading').html())
            .kendoWindow({
                width: '820px',
                content: '${createLink(action: 'edit')}/' + this.dataItem($(e.currentTarget).closest("tr")).id,
                modal: true,
                close: function (e) {
                }
            }).data('kendoWindow').title('${message(code:'serviceDefinition.edit')}').center().open().bind("refresh", function () {
            $('#formWindow').data('kendoWindow').center().open();
        });
    }

    function saveItem() {
        $.ajax({
            url: "${createLink(action: 'save', params: [id: params.id])}",
            dataType: "json",
            data: $('#itemForm').serialize(),
            type: "POST",
            success: function (response) {
                if (response == '1') {
                    var documentListView = $('#grid').data('kendoGrid');
                    documentListView.dataSource.read();
                    documentListView.refresh();
                    $('#formWindow').data('kendoWindow').close();
                }
                else
                    window.alert('${message(code:'serviceDefinition.save.error')}');

            },
            error: function () {
                window.alert('${message(code:'serviceDefinition.save.error')}');
            }
        });
    }

    function cancelEdit() {
        $('#formWindow').data('kendoWindow').close();
    }

    var idForDelete = 0;
    function removeGridItem(e) {
        if (idForDelete == 0) {
            idForDelete = this.dataItem($(e.currentTarget).closest("tr")).id;
            confirm('${message(code:'serviceDefinition.delete.confirm')}', deleteItem, cancelDelete);
        }

    }

    function cancelDelete() {
        idForDelete = 0;
    }

    function deleteItem() {
        if (idForDelete > 0) {
            $.ajax({
                type: "POST",
                url: '${createLink(action: 'delete')}',
                data: {id: idForDelete}
            }).done(function (response) {
                if (response == "1") {
                    var documentListView = $('#grid').data('kendoGrid');
                    documentListView.dataSource.read();
                    documentListView.refresh();
                }
                else {
                    window.alert('${message(code:'serviceDefinition.delete.error')}');
                }
            });
            idForDelete = 0;
        }
    }

    function parameterList(e) {
        window.location.href = '${createLink(controller: 'serviceParameter', action: 'list')}/' + this.dataItem($(e.currentTarget).closest("tr")).id;
    }

    function instanceList(e) {
        window.location.href = '${createLink(controller: 'serviceInstance', action: 'list')}/' + this.dataItem($(e.currentTarget).closest("tr")).id;
    }
</script>
</body>
</html>