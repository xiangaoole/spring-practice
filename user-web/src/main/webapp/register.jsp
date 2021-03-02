<head>
  <jsp:directive.include file="/WEB-INF/jsp/prelude/include-head-meta.jspf" />
  <title>Welcome</title>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <link rel="stylesheet" type="text/css" href="/static/css/register.css" />
  <title>Register Pages</title>
</head>
<body>
  <form action="/RegisterServlet" method="post">
    <table>
      <tr>
        <td class="alignRight">Name:</td>
        <td>
          <input type="text" id="username" name="username" placeholder="请输入用户名" required />
        </td>
      </tr>
      <tr>
        <td class="alignRight">Password:</td>
        <td>
          <input
            type="password"
            name="password"
            id="password"
            placeholder="请输入密码"
            required
          />
        </td>
      </tr>
      <tr>
        <td class="alignRight">Confirm-Password:</td>
        <td>
          <input
            type="password"
            name="repeat_password"
            id="repeat_password"
            placeholder="请确认密码"
            required
          />
        </td>
      </tr>
      <tr>
        <td class="alignRight">Phone-Number:</td>
        <td>
          <input type="text" name="phone_number" id="phone_number" placeholder="请输入手机号码" />
        </td>
      </tr>
      <tr>
        <td class="alignRight">Email:</td>
        <td>
          <input
            type="email"
            name="email"
            id="email"
            class="form-control"
            placeholder="请输入电子邮件"
            required
          />
        </td>
      </tr>
    </table>
    <input type="submit" value="Register" class="submit" />
  </form>
</body>
