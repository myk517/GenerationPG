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
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.Properties;

public class AllCopyPg {
	public static void main(String[] args) throws IOException {
		
		//basePath 구하기
		Properties prop = new Properties();
		InputStream fisProp = new FileInputStream("C:/Users/E4/Desktop/workspace/copyPg/src/util/copyPg.properties");
		prop.load(fisProp);
		
		String basePath = prop.getProperty("basePath");
		String originPath = prop.getProperty("originPath");

		//= = 전체 반복 = =
		//1.읽을 파일의 경로 (txt파일) //읽을파일
		InputStream fisTxt = new FileInputStream("C:/Users/E4/Desktop/workspace/copyPg/src/util/fileList.txt");
			
		//2.txt파일에서 원하는 값 가져오기(한 줄 읽기) //읽을파일
		InputStreamReader fisr = new InputStreamReader(fisTxt);
		BufferedReader br = new BufferedReader(fisr);
		System.out.println("br >> " + br.readLine());
		String originFilePath = br.readLine(); // /oauth/2.0/token
		// if(originFilePath.indexOf("2.0")>0){
		// 	originFilePath = "";
		// }
		//3.복사될 파일경로
		String middlePath = "";
		if(originPath.indexOf("Controller") > 0){
			middlePath = "/controller";
		} else if(originPath.indexOf("Service") >0){
			middlePath = "/service";
		}
		String copyFilePath = basePath + middlePath + originFilePath; //oauth에서 2.0은 제할것.
		System.out.println("basePath>> "+ basePath + "\n middelPath>> " + middlePath + "\n originFilePath >> " + originFilePath + "\n");
		System.out.println("copyFilePath>> "+ copyFilePath);
		System.out.println("originPath >> "+ originPath);
		System.out.println("basePath >> "+ basePath);

		//4.파일명 바꾸기 -> all copy일 경우에는 fileList.txt에서 읽은 경로 앞글자를 대문자로 바꿔서... bla.bla...
		int basePathLen = basePath.length();
		String newPath = copyFilePath.substring(basePathLen);// '/account/Test2.java'
		String newClassNm = newPath.substring(newPath.lastIndexOf("/")+1);
		String firstCh = newClassNm.substring(0,1);
		String remCh = newClassNm.substring(1, newClassNm.length());
		newClassNm = firstCh.toUpperCase() + remCh;
		System.out.println("newClassNm>> " + newClassNm);
		//end of 파일명 바꾸기 수정...

		//5.읽을 파일의 객체 생성: txt파일 File oriFile = new File(oriFilePath)	-> 기존 Controller, Service
			 Properties prop2 = new Properties();
			 prop2.load(fisProp);
		
		File oriFile = new File(originPath);
			// fisProp.close();
		//6.복사파일 객체 생성: java파일 File copyFile = new File(copyFilePath) -> 신규 Controller, Service
		
		String lastName = "";
		if(oriFile.toString().indexOf("Controller")>0){
			lastName = "Controller.java";
			}
		else if(oriFile.toString().indexOf("Service")>0){
			lastName = "Service.java";
		}
			copyFilePath = copyFilePath.substring(0, copyFilePath.lastIndexOf("/")+1);
			System.out.println("copyFilePath>> " + copyFilePath); //~~myapp/app/controller/accout/
			File copyFile = new File(copyFilePath); //경로가 없으면 만들어준다.
			if(!copyFile.isDirectory()){
				copyFile.mkdirs();
			}

			copyFilePath += newClassNm+lastName;
			System.out.println("copyFilePath>> " + copyFilePath); //~~myapp/app/controller/accout/ListController.java
			
			FileInputStream fis2 = new FileInputStream(oriFile); //읽을파일
			System.out.println("oriFIle >> " + oriFile);
			
			copyFile = new File(copyFilePath);
			
			FileOutputStream fos = new FileOutputStream(copyFilePath); 
			br = null;
			BufferedWriter bw = null;
			boolean result = false;

			br = new BufferedReader(new InputStreamReader(fis2));
			bw = new BufferedWriter(new OutputStreamWriter(fos));

			String line; //원본파일에서 읽어들이는 한 라인
			String repLine; //패턴에 일치하는 문자로 대체하고 난 후의 String
			String originalString = ""; //바꾸고자 하는 문자
			String replaceString = ""; //바꿀문자
			
			try{
				//원본파일에서 한 라인 씩 읽는다.
				while((line = br.readLine())!=null){
				// 일치하는 패턴에서는 바꿀 문자로 변환
				// originalString = ""; //OpenBankingService, oeponBankingService, OpenBankingRVO, PVO....
				if(line.equalsIgnoreCase("OpenBanking")){ //openBanking이라는 글자가 들어가있으면..
					originalString = line;
					System.out.println("line >> "+ line);
					replaceString = newClassNm; //카멜케이스화 필요...
				}
				repLine = line.replaceAll(originalString, replaceString);
				//새로운 파일에 쓴다.
				bw.write(repLine, 0, repLine.length());
				bw.newLine();
				}
			} catch(IOException e){
				e.printStackTrace();
			} finally {
				br.close();
				bw.close();
			}
		//7.문자(클래스명 등) 대체 코드


		//== == 여기까지 전체 반복 = =


		
		
		
	}
}
