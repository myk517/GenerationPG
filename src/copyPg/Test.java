package copyPg;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.util.Properties;

public class Test {
	
	public static void main(String[] args) throws IOException {
		
	/**
	 * 
		//1) 읽기
		//Properties활용 위한 객체 선언
		Properties prop = new Properties();
		//읽을 파일의 경로
		InputStream fis = new FileInputStream("C:/Users/E4/Desktop/workspace/copyPg/src/util/test.properties");
		//해당파일 Load
		prop.load(fis);
		//stream닫아주기
		// fis.close();
		//읽어들인 properties파일에서 원하는 값 가져오기.
		//ROOT_PATH는 properties에 정의해놓은 key.
		String sRootPath1 = prop.getProperty("hello");
		String sRootPath2 = prop.getProperty("onePlusOne");

		System.out.println("ROOT PATH1: " + sRootPath1);
		System.out.println("ROOT PATH2: " + sRootPath2);

		//2) 쓰기
		//Properties활용 위한 객체 선언. 생략.
		//읽을 파일의 경로. 생략.
		//해당파일 Load.
		prop.load(fis);
		//stream 닫아주기
		fis.close();
		//파일을 쓰기 위해 FileOutputSteam 이용
		OutputStream fos = new FileOutputStream("C:/Users/E4/Desktop/workspace/copyPg/src/util/write.properties");
		//값을 설정.
		prop.setProperty("key...!", "value...!");
		//저장
		prop.store(fos, "comments...!");
		//스트림 닫기
		fos.close();
		*/	

		//원본 파일경로
		// String oriFilePath = "C:/Users/E4/Desktop/workspace/copyPg/src/copyPg/Test.java";
		//Properties활용 위한 객체 선언
		Properties prop = new Properties();
		//읽을 파일의 경로
		InputStream fis = new FileInputStream("C:/Users/E4/Desktop/workspace/copyPg/src/util/copyPg.properties");
		//해당파일 Load
		prop.load(fis);
		//stream닫아주기
		fis.close();
		//읽어들인 properties파일에서 원하는 값 가져오기 (properties에 정의해놓은 key)
		String originPath = prop.getProperty("originPath");
		String oriFilePath = originPath; //properties에서 읽어서 가져오기.
		//복사될 파일경로
		String copyFilePath = prop.getProperty("targetPath");
		
		/**
		 * 파일명 바꾸기
		 */
		String basePath = prop.getProperty("basePath");
		int basePathLen = basePath.length();
		String newPath = copyFilePath.substring(basePathLen);// '/account/Test2.java'
		String newClassNm = newPath.substring(newPath.lastIndexOf("/")+1);
		System.out.println("newClassNm>> " + newClassNm);
		
		//파일객체생성
		File oriFile = new File(oriFilePath);
		//복사파일객체생성
		File copyFile = new File(copyFilePath);

			FileInputStream fis2 = new FileInputStream(oriFile); //읽을 파일
			FileOutputStream fos = new FileOutputStream(copyFile); //복사할파일
			BufferedReader br = null;
			BufferedWriter bw = null;
			boolean result = false;

			br = new BufferedReader(new InputStreamReader(fis2));
			bw = new BufferedWriter(new OutputStreamWriter(fos));

			String line; //원본파일에서 읽어 들이는 한 라인
			String repLine; //패턴에 일치하는 문자로 대체하고 난 후의 String
			String originalString = "public class AuthorizeController"; //바꾸고자 하는 문자
			String replaceString = "public class " + newClassNm.substring(0, newClassNm.indexOf(".")) ; //바꿀 문자

			try{
			
			//원본파일에서 한 라인씩 읽는다.
			while((line = br.readLine())!=null){
				// 일치하는 패턴에서는 바꿀 문자로 변환
				repLine = line.replaceAll(originalString, replaceString);
				//새로운 파일에 쓴다.
				bw.write(repLine, 0, repLine.length());
				bw.newLine();
			}
			result = true; //정상적으로 수행됐음을 알리는 flag
		} catch(IOException e){
			e.printStackTrace();
		} finally{
			br.close();
			bw.close();
		} 

			// int n = 0;
			// int cnt = 0;
			// int tot = 0; //읽어온 바이트 수

			// byte buf [] = new byte[1024]; //메모리를 일시적으로 저장하기 위함
			// System.out.println(fis2.read(buf)); //바이트 수

			// while((n = fis2.read(buf)) != -1){
				
			// 	fos.write(buf, 0, n);
			// 	fos.flush();
			// 	cnt ++;
			// 	tot =+ n;
			// }
			// System.out.println("cnt : " + cnt + "  " + tot + "바이트가 복사 되었습니다.");
			// System.out.println(fos.toString());
			fis.close();
			fos.close();

			// OutputStreamWriter fos = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("fos"), "UTF-8"));
			
			// int fileByte = 0;
			// //fis.read()가 -1이면 파일을 다 읽은 것
			// while((fileByte = fis.read()) != -1){
			// 	fos.write(fileByte);
			// }
			// //자원사용종료
			// fis.close();
			// fos.close();


	}

}
