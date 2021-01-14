<%@page import="model.BoardDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Insert title here</title>
	</head>
	<body>
		
		<%
		
		request.setCharacterEncoding("UTF-8");
		
		%>
		
		<jsp:useBean id="boardbean" class="model.BoardBean">
			<jsp:setProperty name="boardbean" property="*" />
		</jsp:useBean>
		
		<%
		
		BoardDAO bdao = new BoardDAO();
		
		//해당 게시글의 패스워드값을 얻어옴
		String pass = bdao.getPass(boardbean.getNum());
		
		//기존 패스워드값과 update시 작성했던 패스워드값이 같은지 비교
		if(pass.equals(boardbean.getPassword())){
			
			bdao.updateBoard(boardbean);
			
			response.sendRedirect("BoardList.jsp");
		}
		else{
		%>
		
			<script type="text/javascript">
				alert("패스워드가 일치하지 않습니다.");
				history.go(-1);
			</script>
		
		<%
		}
		%>
		
	</body>
</html>