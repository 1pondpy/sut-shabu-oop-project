<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login - SUT Shabu</title>
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+Thai:wght@100..900&display=swap" rel="stylesheet">
    <style>
                body {
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    height: 100vh;
                    background: #2D2D2D;
                }

                * {
                    font-family: 'Noto Sans Thai', sans-serif;
                }

                .login-card {
                    background: #FFDB58;
                    padding: 40px;
                    border-radius: 20px;
                    box-shadow: 0 4px 15px rgba(0,0,0,0.1);
                    width: 350px;
                    text-align: center;
                }

                .form-control {
                    width: 90%;
                    padding: 10px 0 10px 20px;
                    margin: 20px 0;
                    border-radius: 20px;
                    border: solid 0.5px white;
                    font-family: 'Noto Sans Thai', sans-serif;
                }

                .btn-login {
                    width: 80%;
                    padding: 10px;
                    background: #2D2D2D;
                    border: none;
                    border-radius: 60px;
                    cursor: pointer;
                    font-weight: bold;
                    color: #ffffff;
                    margin-top: 20px;
                    font-family: 'Noto Sans Thai', sans-serif;
                }

                .error {
                    color: red;
                    font-size: 14px;
                    margin-bottom: 10px;
                }
                ::placeholder {
                font-family: 'Noto Sans Thai', sans-serif;
                color: #666;
                font-weight: 500;
                
                }
                .form-control:focus {
                    outline: none; 
                    border-color: #FFDB58; 
                    box-shadow: 0 0 5px rgba(0,0,0,0.2); 
                }

    </style>
</head>
<body>
    <div class="login-card">
        <img src="images/logoblack.png" style="width: 100px; margin-bottom: 20px;">
    
        <% if (request.getAttribute("error") != null) { %>
            <div class="error"><%= request.getAttribute("error") %></div>
        <% } %>

        <form action="login" method="post">
            <input type="text" name="username" class="form-control" placeholder="Username" required >
            <input type="password" name="password" class="form-control" placeholder="Password" required>
            <button type="submit" class="btn-login">Login</button>
        </form>
    </div>
</body>
</html>