package com.fy.tnzbsq.bean;

import com.tgb.lk.ahibernate.annotation.Column;
import com.tgb.lk.ahibernate.annotation.Table;

import java.io.Serializable;

@Table(name = "user")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	@Column(name = "id")
	public int id;
	/**
	 * 用户名/账号
	 */
	@Column(name = "user_id")
	public String user_id;
	/**
	 * 密码
	 */
	@Column(name = "pwd")
	public String pwd;
	/**
	 * 电话
	 */
	@Column(name = "tel")
	public String tel;
	/**
	 * QQ
	 */
	public String qq;
	/**
	 * 用户头像
	 */
	public String avatar;
	/**
	 * 角色
	 */
	@Column(name = "role")
	public int role;
	/**
	 * 最新登录时间
	 */
	@Column(name = "now_login_time")
	public String now_login_time;
	
	/**
	 * 最后登录时间
	 */
	@Column(name = "last_login_time")
	public String last_login_time;
	/**
	 * 串号
	 */
	@Column(name = "mime")
	public String mime;
	/**
	 * 注册时间
	 */
	@Column(name = "reg_time")
	public String reg_time;
	
	/**
	 * 登录次数
	 */
	@Column(name = "login_num")
	public int login_num;
	/**
	 * 默认用户姓名
	 */
	@Column(name = "def_name")
	public String def_name;
	/**
	 * 默认手机号码
	 */
	@Column(name = "def_tel")
	public String def_tel;
	/**
	 * 默认昵称
	 */
	@Column(name = "def_nickname")
	public String def_nickname;
	/**
	 * 默认英文名称
	 */
	@Column(name = "def_e_name")
	public String def_e_name;
	/**
	 * 好友昵称
	 */
	@Column(name = "friend_nickname")
	public String friend_nickname;
	/**
	 * 是否填充用户名
	 */
	@Column(name = "fill_name")
	public int fill_name;

	/**
	 * 性别
	 */
	@Column(name = "gender")
	public String gender;

	/**
	 * 登录类别 1qq，2微信
	 */
	@Column(name = "login_type")
	public String login_type;

	@Column(name = "open_id")
	public String open_id;

	@Column(name = "nickname")
	public String nickname;

	@Column(name = "logo")
	public String logo;

	@Column(name = "imeil")
	public String imeil;

	@Column(name = "is_vip")
	public int is_vip;
}
