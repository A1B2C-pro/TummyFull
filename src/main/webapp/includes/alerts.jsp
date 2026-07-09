<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%
    // Session-stored flash messages (set by a servlet right before redirect)
    // are pulled into request scope here so a single <c:if> covers both cases,
    // then cleared so they don't reappear on the next page view.
    Object sessErr = session.getAttribute("errorMsg");
    Object sessSucc = session.getAttribute("successMsg");
    if (sessErr != null) { request.setAttribute("errorMsg", sessErr); session.removeAttribute("errorMsg"); }
    if (sessSucc != null) { request.setAttribute("successMsg", sessSucc); session.removeAttribute("successMsg"); }
%>
<c:if test="${not empty errorMsg}">
    <div class="alert alert-error">⚠ ${errorMsg}</div>
</c:if>
<c:if test="${not empty successMsg}">
    <div class="alert alert-success">✓ ${successMsg}</div>
</c:if>
