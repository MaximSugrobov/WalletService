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
        <h2>Wallet Service registration form</h2>
    </div>
    <form action="/register_result" method="post" accept-charset="utf-8">
        <table align="center">
            <tr>
                <td>First name : </td>
                <td><input type="text" name="first_name"/></td>
            </tr>
            <tr>
                <td>Last name : </td>
                <td><input type="text" name="last_name"/></td>
            </tr>
            <tr>
                <td>Login : </td>
                <td><input type="text" name="username"/></td>
            </tr>
            <tr>
                <td>Password : </td>
                <td><input type="password" name="password" autocomplete="off"/></td>
            </tr>
        </table>
        <p align="center"><input type="submit" value="Continue"/></p>
    </form>
</body>
</html>