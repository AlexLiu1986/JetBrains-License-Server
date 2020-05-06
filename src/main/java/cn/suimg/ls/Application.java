package cn.suimg.ls;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RestController
@SpringBootApplication
@RequestMapping(value="/",produces="text/xml; charset=UTF-8")
public class Application {

	/**
	 * 授权的用户名
	 */
	private static String username;

	public static void main(String[] args) {
		try{
			username=args[0];
		}catch (ArrayIndexOutOfBoundsException e){
			System.err.println("启动方式:java -Dserver.port=80 -jar license-server.jar 要授权的用户名");
			System.err.println("注意:1.如果不配置启动端口默认会尝试8080端口,如果不加启动参数将会导致启动失败!");
			System.exit(0);
		}
		SpringApplication.run(Application.class, args);
	}

	/**
	 * Index
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/",produces="text/html; charset=UTF-8")
	public void index(HttpServletResponse response) throws IOException {
		StreamUtils.copy(new ClassPathResource("index.html").getInputStream(),response.getOutputStream());
	}

	/**
	 * @return
	 */
	@RequestMapping("rpc/obtainTicket.action")
	public Object obtainTicket(String salt){
		return JetBrainsUtil.obtainTicket(salt,username);
	}

	/**
	 * ReleaseTicket
	 * @param salt
	 * @return
	 */
	@RequestMapping("rpc/releaseTicket.action")
	public Object releaseTicket(String salt){
		return JetBrainsUtil.releaseTicket(salt);
	}

	/**
	 * ProlongTicket
	 * @param salt
	 * @return
	 */
	@RequestMapping("rpc/prolongTicket.action")
	public Object prolongTicket(String salt){
		return JetBrainsUtil.prolongTicket(salt);
	}

	/**
	 * Ping
	 * @param salt
	 * @return
	 */
	@RequestMapping("rpc/ping.action")
	public Object ping(String salt){
		return JetBrainsUtil.ping(salt);
	}

}
