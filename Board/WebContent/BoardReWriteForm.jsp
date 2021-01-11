<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Insert title here</title>
	</head>
	<body>
	
		<h2>답변글 입력하기</h2>
	
		<%
		//BoardInfo에서 답변쓰기를 클릭하면 넘겨주는 데이터들을 받아줌
		int num = Integer.parseInt(request.getParameter("num"));
		int ref = Integer.parseInt(request.getParameter("ref"));
		int re_step = Integer.parseInt(request.getParameter("re_step"));
		int re_level = Integer.parseInt(request.getParameter("num"));
		
		%>
		
		
			<table width="600" border="1" bgcolor="lightgray">
			<form action="BoardReWriteProc.jsp" method="post">
				<tr height="40">
					<td align="center" width="150">작성자</td>
					<td width="450">&nbsp;<input type="text" name="Writer" size="60"></td>
				</tr>
				<tr height="40">
					<td align="center" width="150">제목</td>
					<td width="450">&nbsp;<input type="text" name="sebject" value="[답변] " size="60"></td>
				</tr>
				<tr height="40">
					<td align="center" width="150">이메일</td>
					<td width="450">&nbsp;<input type="email" name="email" size="60"></td>
				</tr>
				<tr height="40">
					<td align="center" width="150">비밀번호</td>
					<td width="450">&nbsp;<input type="password" name="password" size="60"></td>
				</tr>
				<tr height="40">
					<td align="center" width="150">글내용</td>
					<td width="450"><textarea rows="15" cols="60" name="content"></textarea></td>
				</tr>
				
				<!-- form에서 사용자로부터 입력받지 않고 데이터 넘기기 -->
				<tr height="40">
					<td align="center" colspan="2">
						<input type="hidden" name="ref" value="<%=ref%>">
						<input type="hidden" name="re_step" value="<%=re_step%>">
						<input type="hidden" name="re_level" value="<%=re_level%>">
						<input type="submit" value="답글쓰기">&nbsp;&nbsp;
						<input type="reset" value="다시작성">&nbsp;&nbsp;
					</form>
						<input type="button" onclick="location.href='BoardList.jsp'" value="목록으로">
					</td>
				</tr>
			
			
			</table>
			
		
		
		
	</body>
</html>