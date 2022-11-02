<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Wallet Service</title>
</head>
<body>
    <div align="center">
        <h2>Wallet Service</h2>
    </div>
    <form action="/login" method="post" accept-charset="utf-8">
        <table align="center">
            <tr>
                <td>Login : </td>
                <td><input type="text" name="username"/></td>
            </tr>
            <tr>
                <td>Password : </td>
                <td><input type="password" name="password" autocomplete="off"/></td>
            </tr>
        </table>
        <p align="center"><input type="submit" value="Submit"/></p>
    </form>
    <p align="center">New user? <a href="forms/register.jsp">Sign in here</a></p>
</body>
</html>