<%----------------------------------------------------------------------------->
< This is an html component intended to display information passed to a page
< through the request. To be inserted temporarily in a page to see what's going
< on.
< Loosely based on snoop.jsp (Copyright (c) 1999 The Apache Software Foundation. 
<                             All rights reserved).
<-----------------------------------------------------------------------------%>
<%@ page import="java.util.*"
         import="java.lang.*" %>

<%  Enumeration names;
    String name; %>

<table border width = 100%>
    <tr><td align="center"><h2> Debug Information </h2></td><tr>
</table>

<h3> Request Information </h3>
<table>
<tr><td align=right><b>Attributes:</b></td>
<%  names = request.getAttributeNames();
    if (names.hasMoreElements()) {
        name = (String)names.nextElement(); %>
        <td><u><%= name %></u> = <%= request.getAttribute(name) %></td>
<%  } else { %>
        <td>none</td>
<%  } %>
</tr>

<%  while (names.hasMoreElements()) {
        name = (String)names.nextElement(); %>
        <tr><td></td><td><u><%= name %></u> = <%= request.getAttribute(name) %></td></tr>
<%  }  %>
<tr><td align=right><b>Authorization Type:</b></td><td><%= request.getAuthType() %></td></tr>
<tr><td align=right><b>Character Encoding:</b></td><td><%= request.getCharacterEncoding() %></td></tr>
<tr><td align=right><b>Content Length:</b></td><td><%= request.getContentLength() %></td></tr>
<tr><td align=right><b>Content Type:</b></td><td><%= request.getContentType() %></td></tr>
<tr><td align=right><b>Headers:</b></td>
<%  names = request.getHeaderNames();
    if (names.hasMoreElements()) {
        name = (String)names.nextElement(); %>
        <td><u><%= name %></u> = <%= request.getHeader(name) %></td>
<%  } else { %>
        <td>none</td>
<%  } %>
</tr>

<%  while (names.hasMoreElements()) {
        name = (String)names.nextElement(); %>
        <tr><td></td><td><u><%= name %></u> = <%= request.getHeader(name) %></td></tr>
<%  }  %>
<tr><td align=right><b>Is Secure:</b></td><td><%= request.isSecure() %></td></tr>
<tr><td align=right><b>Locale:</b></td><td><%= request.getLocale() %></td></tr>
<tr><td align=right><b>Method:</b></td><td><%= request.getMethod() %></td></tr>
<tr><td align=right><b>Parameters:</b></td>
<%  names = request.getParameterNames();
    if (names.hasMoreElements()) {
        name = (String)names.nextElement(); %>
        <td><u><%= name %></u> = <%= request.getParameter(name) %></td>
<%  } else { %>
        <td>none</td>
<%  } %>
</tr>

<%  while (names.hasMoreElements()) {
        name = (String)names.nextElement(); %>
        <tr><td></td><td><u><%= name %></u> = <%= request.getParameter(name) %></td></tr>
<%  }  %>
<tr><td align=right><b>Path Info:</b></td><td><%= request.getPathInfo() %></td></tr>
<tr><td align=right><b>Protocol:</b></td><td><%= request.getProtocol() %></td></tr>
<tr><td align=right><b>Query String:</b></td><td><%= request.getQueryString() %></td></tr>
<tr><td align=right><b>Remote Address:</b></td><td><%= request.getRemoteAddr() %></td></tr>
<tr><td align=right><b>Remote Host:</b></td><td><%= request.getRemoteHost() %></td></tr>
<tr><td align=right><b>Remote User:</b></td><td><%= request.getRemoteUser() %></td></tr>
<tr><td align=right><b>Request Protocol:</b></td><td><%= request.getProtocol() %></td></tr>
<tr><td align=right><b>Request URI:</b></td><td><%= request.getRequestURI() %></td></tr>
<tr><td align=right><b>Scheme:</b></td><td><%= request.getScheme() %></td></tr>
<tr><td align=right><b>Server Name:</b></td><td><%= request.getServerName() %></td></tr>
<tr><td align=right><b>Server Port:</b></td><td><%= request.getServerPort() %></td></tr>
<tr><td align=right><b>Servlet Path:</b></td><td><%= request.getServletPath() %></td></tr>
<tr><td align=right><b>Session:</b></td><td><%= request.getSession(false) %></td></tr>
</table>

<h3> Application Information </h3>

<table>
<tr><td align=right><b>Attributes:</b></td>
<%  names = application.getAttributeNames();
    if (names.hasMoreElements()) {
        name = (String)names.nextElement(); %>
        <td><u><%= name %></u> = <%= application.getAttribute(name) %></td>
<%  } else { %>
        <td>none</td>
<%  } %>
</tr>

<%  while (names.hasMoreElements()) {
        name = (String)names.nextElement(); %>
        <tr><td></td><td><u><%= name %></u> = <%= application.getAttribute(name) %></td></tr>
<%  }  %>
<tr><td align=right><b>Init Parameters:</b></td>
<%  names = application.getInitParameterNames();
    if (names.hasMoreElements()) {
        name = (String)names.nextElement(); %>
        <td><u><%= name %></u> = <%= application.getInitParameter(name) %></td>
<%  } else { %>
        <td>none</td>
<%  } %>
</tr>

<%  while (names.hasMoreElements()) {
        name = (String)names.nextElement(); %>
        <tr><td></td><td><u><%= name %></u> = <%= application.getInitParameter(name) %></td></tr>
<%  }  %>
<tr><td align=right><b>Server Info:</b></td><td><%= application.getServerInfo() %></td></tr>
<tr><td align=right><b>Servlet Context Name:</b></td><td><%= application.getServletContextName() %></td></tr>
</table>
<hr>
