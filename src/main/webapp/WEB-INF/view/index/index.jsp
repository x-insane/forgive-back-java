<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>原谅块</title>
    <link href="https://cdn.bootcss.com/bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet">
    <link rel="shortcut icon" href="https://static.gotohope.cn/img/favicon.ico" type="image/x-icon" />
    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no">
    <style>
        h4 {
            width: 100%;
            text-align: center;
            margin-top: 28px;
            margin-bottom: 22px;
        }
    </style>
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/">原谅块</a>
        <ul class="nav">
            <li class="nav-item dropdown">
                <a data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">
                    <svg style="margin-top: 9px" aria-hidden="true" height="22" version="1.1" viewBox="0 0 16 16" width="22"><path fill-rule="evenodd" d="M8 0C3.58 0 0 3.58 0 8c0 3.54 2.29 6.53 5.47 7.59.4.07.55-.17.55-.38 0-.19-.01-.82-.01-1.49-2.01.37-2.53-.49-2.69-.94-.09-.23-.48-.94-.82-1.13-.28-.15-.68-.52-.01-.53.63-.01 1.08.58 1.23.82.72 1.21 1.87.87 2.33.66.07-.52.28-.87.51-1.07-1.78-.2-3.64-.89-3.64-3.95 0-.87.31-1.59.82-2.15-.08-.2-.36-1.02.08-2.12 0 0 .67-.21 2.2.82.64-.18 1.32-.27 2-.27.68 0 1.36.09 2 .27 1.53-1.04 2.2-.82 2.2-.82.44 1.1.16 1.92.08 2.12.51.56.82 1.27.82 2.15 0 3.07-1.87 3.75-3.65 3.95.29.25.54.73.54 1.48 0 1.07-.01 1.93-.01 2.2 0 .21.15.46.55.38A8.013 8.013 0 0 0 16 8c0-4.42-3.58-8-8-8z"></path></svg>
                </a>
                <div class="dropdown-menu" style="transform: translateX(-95px);">
                    <a class="dropdown-item" target="_blank" href="https://github.com/x-insane/forgive">安卓</a>
                    <a class="dropdown-item" target="_blank" href="https://github.com/x-insane/forgive-back-java">后台&网页（Java版）</a>
                    <a class="dropdown-item" target="_blank" href="https://github.com/x-insane/forgive-back">后台&网页（PHP版）</a>
                </div>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/download/forgive.apk">
                    <svg style="margin: 2px 0 0 5px" viewBox="0 0 1024 1024" version="1.1" width="20" height="20"><path d="M995.84 1024 28.16 1024C12.8 1024 0 1011.2 0 995.84l0 0c0-15.36 12.8-28.16 28.16-28.16l967.68 0c15.36 0 28.16 12.8 28.16 28.16l0 0C1024 1011.2 1011.2 1024 995.84 1024z" p-id="2044" fill="#000000"></path><path d="M926.72 376.32 926.72 376.32c-10.24-10.24-30.72-10.24-40.96 0L537.6 721.92 537.6 28.16C537.6 12.8 527.36 0 512 0s-25.6 12.8-25.6 28.16l0 693.76L138.24 376.32c-10.24-10.24-30.72-10.24-40.96 0-10.24 10.24-10.24 28.16 0 40.96l394.24 394.24c2.56 2.56 2.56 2.56 5.12 2.56 0 0 2.56 2.56 2.56 2.56 7.68 2.56 15.36 2.56 23.04 0 2.56 0 2.56-2.56 2.56-2.56 2.56 0 5.12-2.56 5.12-2.56l394.24-394.24C936.96 404.48 936.96 386.56 926.72 376.32z" p-id="2045" fill="#000000"></path></svg>
                </a>
            </li>
        </ul>
    </div>
</nav>

<div class="container">
    <div class="row">
        <h4>原谅区</h4>
        <table class="table">
            <thead>
            <tr>
                <th scope="col">#</th>
                <th scope="col">关卡</th>
                <th scope="col">最高分</th>
                <th scope="col">擂主</th>
            </tr>
            </thead>
            <tbody>
            <% int i = 1; %>
            <c:forEach items="${requestScope.gameList}" var="game">
                <c:if test="${game.best != null && game.best.type == 'score'}">
                    <tr>
                        <th scope="row"><%= i++ %></th>
                        <td>${game.name}</td>
                        <td>${game.best.score}</td>
                        <td>${game.best.user.nickname}</td>
                    </tr>
                </c:if>
            </c:forEach>
            </tbody>
        </table>
    </div>

    <div class="row">
        <h4>挑战区</h4>
        <table class="table">
            <thead>
            <tr>
                <th scope="col">#</th>
                <th scope="col">关卡</th>
                <th scope="col">最快速度</th>
                <th scope="col">擂主</th>
            </tr>
            </thead>
            <tbody>
            <% i = 1; %>
            <c:forEach items="${requestScope.gameList}" var="game">
                <c:if test="${game.best != null && game.best.type == 'velocity'}">
                    <tr>
                        <th scope="row"><%= i++ %></th>
                        <td>${game.name}</td>
                        <td>${game.best.velocity}</td>
                        <td>${game.best.user.nickname}</td>
                    </tr>
                </c:if>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<script src="https://cdn.bootcss.com/jquery/3.3.1/jquery.min.js"></script>
<script src="https://cdn.bootcss.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>

</body>
</html>
