package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class BoardDAO {
	
	Connection conn;
	PreparedStatement pstmt;
	ResultSet rs;
	
	//데이터베이스의 커넥션풀을 사용하도록 설정하는 메소드
	public void getCon() {
		
		try {
			
			Context initctx = new InitialContext();
			Context envctx = (Context)initctx.lookup("java:comp/env");
			DataSource ds = (DataSource) envctx.lookup("jdbc/pool");
			//datasource
			conn = ds.getConnection();
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	//하나의 새로운 게시글이 넘어와서 저장되는 메소드
	public void inserBoard(BoardBean bean) {
		
		getCon();
		//빈 클래스에 넘어오지 않았던 데이터들을 초기화 해주어야 합니다.
		int ref = 0;// 글 그룹을 의미 = 쿼리를 실행시켜 가장큰 ref 값을 가져온후 +1을 더해주면 된다 
		int re_step = 1;//새글이기에 = 부모 글이기에
		int re_level = 1;
		try {
			//가장큰 ref값을 읽어오는 쿼리 준비 
			String refSQL = "SELECT max(ref) FROM BOARD";
			//쿼리실행 객체 
			pstmt = conn.prepareStatement(refSQL);
			//쿼리를 실행후 결과를 리턴
			 rs = pstmt.executeQuery();	
			if(rs.next()){//결과 값이 있다면
				ref = rs.getInt(1)+1;//최대 값에 +1을 더해서 글 그룹을 설정	
			}
			//실제로 게시글 전체값을 테이블에 저장 
			String SQL = "INSERT INTO BOARD VALUES(board_seq.NEXTVAL,?,?,?,?,sysdate,?,?,?,0,?)";
			pstmt = conn.prepareStatement(SQL);
			//?의 값을 맵핑
			pstmt.setString(1, bean.getWriter());
			pstmt.setString(2, bean.getEmail());
			pstmt.setString(3, bean.getSebject());
			pstmt.setString(4, bean.getPassword());
			pstmt.setInt(5, ref);
			pstmt.setInt(6, re_step);
			pstmt.setInt(7, re_level);
			pstmt.setString(8, bean.getContent());		
			//쿼리를 실행하시오
			pstmt.executeUpdate();	
			//자원 반납
			conn.close();
			}catch(Exception e) {
				e.printStackTrace();	
		}
		
	}
	
	public Vector<BoardBean> getAllBoard(int start, int end){
		Vector<BoardBean> v = new Vector<>();
		getCon();
		try {
			String sql = "select * from (select A.*, Rownum Rnum from(select * from board order by ref desc, re_level asc)A) " 
					+ "where Rnum >= ? and Rnum <= ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, start);
			pstmt.setInt(2, end);
			rs = pstmt.executeQuery();
			//데이터갯수가 몇개인지 몰르기에 반복문을 이용하여 데이터 추출
			while(rs.next()) {
				//데이터를 패키징(boaedBean클래스를 이용)
				BoardBean bean = new BoardBean();
				bean.setNum(rs.getInt(1));
				bean.setWriter(rs.getString(2));
				bean.setEmail(rs.getString(3));
				bean.setSebject(rs.getString(4));
				bean.setPassword(rs.getString(5));
				bean.setReg_date(rs.getDate(6).toString());
				bean.setRef(rs.getInt(7));
				bean.setRe_step(rs.getInt(8));
				bean.setRe_level(rs.getInt(9));
				bean.setReadcount(rs.getInt(10));
				bean.setContent(rs.getString(11));
				//패키징한 데이터를 백터(v)에 저장
				v.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return v;
	}
	
	public BoardBean getOneBoard(int num) {
		
		//리턴타입선언
		BoardBean bean = new BoardBean();
		getCon();
		
		try {
			//조회수 증가 쿼리
			String readsql = "update board set readcount = readcount+1 where num=?"; //set -> 바꾼다
			pstmt = conn.prepareStatement(readsql);
			pstmt.setInt(1, num);
			pstmt.executeUpdate();
			
			String sql = "select * from board where num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			//쿼리 실행 후 결과를 리턴 
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				bean.setNum(rs.getInt(1));
				bean.setWriter(rs.getString(2));
				bean.setEmail(rs.getString(3));
				bean.setSebject(rs.getString(4));
				bean.setPassword(rs.getString(5));
				bean.setReg_date(rs.getDate(6).toString());
				bean.setRef(rs.getInt(7));
				bean.setRe_step(rs.getInt(8));
				bean.setRe_level(rs.getInt(9));
				bean.setReadcount(rs.getInt(10));
				bean.setContent(rs.getString(11));
			}
			
			conn.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return bean;
	}
	
	
	public void reWriteBoard(BoardBean bean) {
		
		//부모글 그룹과 글레벨 글 스템을 읽어드림 
				int ref =bean.getRef();
				int re_step = bean.getRe_step();
				int re_level = bean.getRe_level();
				
				getCon();
				
				try {
					/////////////////////핵심 코드 //////////////////////// 
						//부모글 보다 큰 re_level의 값을 전부 1씩 증가시켜줌 
						String  levelsql = "update board set re_level=re_level+1 where ref=? and re_level > ?";
						//쿼리 삽입 객체 선언 
						pstmt = conn.prepareStatement(levelsql);
						pstmt.setInt(1 , ref);
						pstmt.setInt(2 , re_level);
						//쿼리 실행 
						pstmt.executeUpdate();
						//답변글 데이터를 저장
						String sql ="insert into board values(board_seq.NEXTVAL,?,?,?,?,sysdate,?,?,?,0,?)";
						pstmt = conn.prepareStatement(sql);
						//?에 값을 대입
						pstmt.setString(1, bean.getWriter());
						pstmt.setString(2, bean.getEmail());
						pstmt.setString(3, bean.getSebject());
						pstmt.setString(4, bean.getPassword());
						pstmt.setInt(5, ref);//부모의 ref 값을 넣어줌 
						pstmt.setInt(6, re_step+1);//답글이기에 부모글 re_stop에 1을 넣어줌 
						pstmt.setInt(7, re_level + 1);
						pstmt.setString(8, bean.getContent());
						
						//쿼리를 실행하시오
						pstmt.executeUpdate();	
						conn.close();
						
				}catch(Exception e){
					e.printStackTrace();
		}
		
	}
	
	//boardupdate, delete시 하나의 게시글을 리턴
	public BoardBean getOneUpdateBoard(int num) {
		
		//리턴타입선언
		BoardBean bean = new BoardBean();
		getCon();
		
		try {
			
			String sql = "select * from board where num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			//쿼리 실행 후 결과를 리턴 
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				bean.setNum(rs.getInt(1));
				bean.setWriter(rs.getString(2));
				bean.setEmail(rs.getString(3));
				bean.setSebject(rs.getString(4));
				bean.setPassword(rs.getString(5));
				bean.setReg_date(rs.getDate(6).toString());
				bean.setRef(rs.getInt(7));
				bean.setRe_step(rs.getInt(8));
				bean.setRe_level(rs.getInt(9));
				bean.setReadcount(rs.getInt(10));
				bean.setContent(rs.getString(11));
			}
			
			conn.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return bean;
	}
	
	public String getPass(int num) {
		
		//리턴할 변수 객체 선언
		String pass = "";
		getCon();
		
		try {
			
			String sql = "select password from board where num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();
			
			//패스워드값 저장
			if(rs.next()) {
				pass = rs.getString(1);
			}
			conn.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pass;
	}
	
	public void updateBoard(BoardBean bean) {
		
		getCon();
		try {
			
			String sql = "update board set subject=? , content=? where num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, bean.getSebject());
			pstmt.setString(2, bean.getContent());
			pstmt.setInt(3, bean.getNum());
			pstmt.executeUpdate();
			conn.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	public void deleteBoard(int num) {
		
		getCon();
		
		try {
			
			String sql = "delete from board where num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			pstmt.executeUpdate();
			conn.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	//전체글의 갯수를 리턴하는 메서드
	public int getAllCount() {
		getCon();
		
//		게시글 전체수를 저장하는 변수
		int count = 0;
		
		try {
			
			String sql = "select count(*) from board";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				count = rs.getInt(1);//전체 게시글 수 리턴
				conn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

}

















