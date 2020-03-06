package bookshop.bean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class LogonDBBean {
	// LogonDBBean 전역 객체 생성←한 개의 객체만 생성해서 공유
	private static LogonDBBean instance = new LogonDBBean();

//LogonDBBean 객체를 리턴하는 메소드
	public static LogonDBBean getInstance() {
		return instance;
	}

	private LogonDBBean() {
	}

	// 커넥션 풀에서 커넥션 객채를 얻어내는 메소드
	private Connection getConnection() throws Exception {
		Context initCtx = new InitialContext();
		Context envCtx = (Context) initCtx.lookup("java:comp/env");
		DataSource ds = (DataSource) envCtx.lookup("jdbc/Oracle11g");
		return ds.getConnection();
	}

	// 회원가입 처리에서 사용하는 메소드
	public void insertMember(LogonDataBean member) {
		Connection conn = null;
		PreparedStatement pstmt = null;


		try {
			conn = getConnection();


			pstmt = conn.prepareStatement("insert into member values(?,?,?,?,?,?)");
			pstmt.setString(1, member.getId());
			pstmt.setString(2, member.getPasswd());
			pstmt.setString(3, member.getName());
			pstmt.setTimestamp(4, member.getReg_date());
			pstmt.setString(5, member.getAddress());
			pstmt.setString(6, member.getTel());
			pstmt.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException ex) {
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException ex) {
				}
		}
	}

	// 로그인 폼 처리의 사용자 인증 처리에서 사용하는 메소드
	public int userCheck(String id, String passwd) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		int x = -1;

		try {
			conn = getConnection();


			pstmt = conn.prepareStatement("select passwd from member where id=?");
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();

			if (rs.next()) { // 해당 아이디가 있으면 수행
				String dbpasswd = rs.getString("passwd");
				if (dbpasswd.equals(passwd)) {
					x = 1; // 인증 성공
				} else {
					x = 0; // 비밀번호 틀림
				}
			} else { // 해당 아이디가 없으면 수행
				x = -1; // 아이디 없음
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException ex) {
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException ex) {
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException ex) {
				}
		}

		return x;
	}

	// 아이다 중복 확인레서 아이디의 중복 여부를 확인하는 메소드
	public int confirmId(String id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int x = -1;

		try {
			conn = getConnection();

			pstmt = conn.prepareStatement("select id from memeber where id=?");
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();

			if (rs.next()) {// 아이디 존재
				x = 1;// 같은 아이디 있음
			} else {
				x = -1; // 같은 아이디 없음
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException ex) {
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException ex) {
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException ex) {
				}
		}

		return x;
	}

	// 주어진 id에 해당하는 회원정보를 얻어내는 메소드
	public LogonDataBean getMember(String id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		LogonDataBean member = null;

		try {
			conn = getConnection();

			pstmt = conn.prepareStatement("select * from member where id=?");
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();

			if (rs.next()) {// 해당 아이디 레코드가 존재
				member = new LogonDataBean();// 데이터 저장빈 객체생성
				member.setId(rs.getString("id"));
				member.setName(rs.getString("name"));
				member.setReg_date(rs.getTimestamp("reg_date"));
				member.setAddress(rs.getString("address"));
				member.setTel(rs.getString("tel"));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException ex) {
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException ex) {
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException ex) {
				}
		}

		return member;// 데이터 저장빈 객체 member 리턴
	}

	// 주어진 id, passwd에 해당하는 회원정보를 얻어내는 메소드
	public LogonDataBean getMember(String id, String passwd) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		LogonDataBean member = null;


		try {
			conn = getConnection();


			pstmt = conn.prepareStatement("select * from member where id=?");
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();

			if (rs.next()) {// 해당 아이디에 대한 레코드가 존재
				String dbPasswd = rs.getString("passwd");
				if (dbPasswd.equals(passwd)) { // 사용자가 입력한 비밀번호와 같으면 수행
					member = new LogonDataBean(); // 데이터 저장빈 객체 생성
					member.setId(rs.getString("id"));
					member.setName(rs.getString("name"));
					member.setReg_date(rs.getTimestamp("reg_date"));
					member.setAddress(rs.getString("addess"));
					member.setTel(rs.getString("tel"));
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException ex) {
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException ex) {
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException ex) {
				}
		}

		return member;
	}

	// 회원 정보 수정을 처리하는 메소드
	@SuppressWarnings("resource")
	public int updateMember(LogonDataBean member) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int x = -1;


		try {
			conn = getConnection();

			pstmt = conn.prepareStatement("select passwd from member where id=?");
			pstmt.setString(1, member.getId());
			rs = pstmt.executeQuery();

			if (rs.next()) {// 해당 아이디가 있으면 수행
				String dbpasswd = rs.getString("passwd");
				if (dbpasswd != null) {
					pstmt = conn.prepareStatement("update member set name=?, address=?, tel=? where id=?");
					pstmt.setString(1, member.getName());
					pstmt.setString(2, member.getAddress());
					pstmt.setString(3, member.getTel());
					pstmt.setString(4, member.getId());
					pstmt.executeQuery();

					x = 1;// 회원 정보 수정 처리 성공
				} else {
					x = 0;// 회원 정보 수정 처리 실패

				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException ex) {
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException ex) {
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException ex) {
				}
		}

		return x;
	}

	// 회원정보를 삭제하는 메소드
	@SuppressWarnings("resource")
	public int deleteMember(String id, String passwd) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int x = -1;

		try {
			conn = getConnection();

			pstmt = conn.prepareStatement("select passwd from member where id=?");
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				String dbpasswd = rs.getString("passwd");
				if (dbpasswd.equals(passwd)) {
					pstmt = conn.prepareStatement("delete from member where id=?");
					pstmt.setString(1, id);
					pstmt.executeUpdate();

					x = 1;// 회원 탈퇴 처리 성공
				} else {
					x = 0;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException ex) {
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException ex) {
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException ex) {
				}
		}

		return x;
	}
}
