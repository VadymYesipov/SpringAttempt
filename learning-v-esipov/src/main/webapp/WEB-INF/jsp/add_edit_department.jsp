<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>

<html>

<c:set var="title" value="Login" />
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<body>

<table id="main-container">

    <tr>

        <td class="content center">
            <form class="login_form" action="controller" method="post">
                <input type="hidden" name="command" value="addDepartment"/></p>
                <input type="text" value="${add_name}" name="departmentName" required pattern="^[A-ZА-Я][a-zа-яё\s]+" placeholder="<fmt:message key="index.jsp.placeholder.departmentName"/>"/></p>
                <input type="submit" value="<fmt:message key="index.jsp.submit.add"/>"/>
            </form>
        </td>

        <td class="content center">
            <form class="login_form" action="controller" method="post">
                <input type="hidden" name="command" value="editDepartment"/>
                <input type="number" value="${edit_ID}" required min="1" max="${departmentList.get(departmentList.size() - 1).getId()}"
                       name="id" placeholder="<fmt:message key="index.jsp.placeholder.chooseId"/>" /></p>
                <input type="number" value="${new_edit_ID}" min="1" name="newId"
                       placeholder="<fmt:message key="index.jsp.placeholder.newId"/>" /></p>
                <input type="text" value="${edit_name}" name="departmentName" pattern="^[A-ZА-Я][a-zа-яё\s]+" placeholder="<fmt:message key="index.jsp.placeholder.departmentName"/>" /></p>
                <input type="submit" value="<fmt:message key="index.jsp.submit.edit"/>"/>
            </form>
        </td>

        <td class="content center">
            <form class="login_form" method="post" action="/controller">
                <input type="hidden" required name="command" value="list"/></p>
                <input type="hidden" required name="name" value="departments"/></p>
                <input type="submit" value="<fmt:message key="index.jsp.placeholder.departments"/>"/>
            </form>
        </td>

    </tr>

    <tr>
        <td class="content bottom" colspan="3">
            <%-- if get this page using forward --%>
            <c:if test="${not empty errorMessage and empty exception and empty code}">
                <h3>Error message: ${errorMessage}</h3>
            </c:if>
        </td>
    </tr>

</table>

</body>

</html>