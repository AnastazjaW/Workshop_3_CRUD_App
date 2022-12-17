<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<link href="../vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
<link href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i"
      rel="stylesheet">

<!-- Custom styles for this template-->
<link href="<c:url value="/theme/css/sb-admin-2.css"/>" rel="stylesheet">
<%@ include file="/header.jsp" %>
<div class="container-fluid">

    <!-- Page Heading -->
    <div class="d-sm-flex align-items-center justify-content-between mb-4">
        <h1 class="h3 mb-0 text-gray-800">UsersCRUD</h1>
        <a href="/user/add" class="d-none d-sm-inline-block btn btn-sm btn-primary shadow-sm"><i
                class="fas fa-download fa-sm text-white-50"></i>Dodaj użytkownika</a>
    </div>
    <table>
        <tr>
            <th>ID</th>
            <th>Nazwa użytkownika</th>
            <th>E-mail</th>
            <th>Akcja</th>
        </tr>
        <c:forEach var="user" items="${users}">
            <tr>
                <td>${user.id}</td>
                <td>${user.userName}</td>
                <td>${user.email}</td>
                <td><a href="/user/delete?id=${user.id}">Usuń</a><a href="/user/edit?id=${user.id}">Edytuj</a> <a href="/user/show?id=${user.id}">Pokaż</a></td>
            </tr>
        </c:forEach>
    </table>
</div>
<%@ include file="/footer.jsp" %>