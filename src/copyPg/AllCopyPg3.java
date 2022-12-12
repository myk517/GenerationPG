package copyPg;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
public class AllCopyPg3 {
	public static void main(String[] args) throws IOException {
		//BufferedWriter -> try catch문으로 잡을 것.

		//전체반복
		//1)txt파일 읽어오기
		File fisTxt = new File("src/util/fileList3.txt");
		//1-1)txt파일에서 원하는 값 가져오기(한 줄 읽기), 읽어올 때 부터 인코딩해서 읽어와야 한다.
		BufferedReader br  =  new BufferedReader(new InputStreamReader(new FileInputStream(fisTxt),"UTF-8"));
		String lines;
		while((lines = br.readLine())!=null){
			System.out.println("lines :::: " + lines);
		//1-2) 읽어 온 한 줄을 split으로 나누기
		// String line = br.readLine();
		String[] lineArr = lines.split(",");
		for(int i = 0; i< lineArr.length ; i++){
			System.out.println("lineArr [] >> " + lineArr[i]);
		}
		/**
		 * lineArr[0] = 인터페이스ID ex)OBI-001
		 * lineArr[1] = 기능구분 ex)사용자인증(OAuth 2.0)
		 * lineArr[2] = API명 ex)토큰발급
		 * lineArr[3] = 금결원URI ex)https://openapi.openbanking.or.kr/oauth/2.0/token
		 * lineArr[4] = HTTPMethod ex)POST
		 * lineArr[5] = 요청토큰Scope ex)sa, 해당없음
		 * lineArr[6] = Secta9ine_API_URL ex)/oauth/token 
		 * lineArr[7] = package ex)kr.co.secta9ine.online.openbanking.oauth
		 * lineArr[8] = classId ex)OAuthToken
		 * lineArr[9] = API설명 
		 */

		 //2) 원본Template 파일 읽어오기
		 //2-1) 읽을 파일의 객체 생성: 기존 Controller, Service
		 Properties prop = new Properties();
		 InputStream fisProp = new FileInputStream("C:/Users/E4/Desktop/workspace/copyPg/src/util/copyPg3.properties");
		 prop.load(fisProp);
		 String originPath = prop.getProperty("originPath");
		 String originFileMiddleName = originPath.substring(originPath.lastIndexOf("/"), originPath.lastIndexOf(".java")) ;
		 System.out.println("originFileMiddleName >> " + originFileMiddleName);
		 File originFile = new File(originPath);
		 //2-2)원본 파일에서 한 줄 씩 읽기
		BufferedReader brOrginFile = new BufferedReader(new InputStreamReader(new FileInputStream(originPath), "UTF-8"));
		String originFileLine;
		//3) 새로운 파일에 쓰기
		//3-1) 새로운 파일의 경로 세팅하기
		String basePath = prop.getProperty("basePath");
		String middlePath = ""; //package
		String middleName = ""; //class
		
		if(originFileMiddleName.contains("PVO")){
			middlePath = "vo";
			middleName = "PVO";
		} else if(originFileMiddleName.contains("RVO")){
			middlePath = "vo";
			middleName = "RVO";
		} else if(originFileMiddleName.contains("Controller")){
			middlePath = "controller";
			middleName = "Controller";
		} else if(originFileMiddleName.contains("Service")){
			middlePath = "service";
			middleName = "Service";
		} else {
			System.out.println("originFileMiddleName에서 찾을 수 없습니다.");
		}
		String fileName = lineArr[8] + middleName + ".java";
		String newClassNm = lineArr[8] +  middleName;
		String copyFilePath;
		copyFilePath = (basePath + lineArr[7]+ "/" + middlePath).replace(".", "/");
		System.out.println(" copyFilePath......:::::" +copyFilePath);
		File copyFile = new File(copyFilePath); //경로가 없으면 만들어준다.
			if(!copyFile.isDirectory()){
				copyFile.mkdirs();
			}
		
		copyFilePath = basePath + lineArr[7]+ "/" + middlePath + "/" + lineArr[8] +  middleName + "$java"; 
		//API경로에 fin_num, acnt_num이 포함되어 있으면 VO를 FinNum,ActnNum용으로 분기한다.
			if(lineArr[3].contains("fin_num")){
				copyFilePath = basePath + lineArr[7]+ "/" + middlePath + "/" + lineArr[8] +  "FinNum" + middleName + "$java"; 
				newClassNm = lineArr[8] +  "FinNum" + middleName;
			} else if(lineArr[3].contains("acnt_num")){
				copyFilePath = basePath + lineArr[7]+ "/" + middlePath + "/" + lineArr[8] +  "AcntNum" + middleName + "$java"; 
				newClassNm = lineArr[8] +  "AcntNum" + middleName;
			}
		
		copyFilePath = copyFilePath.replace(".", "/");
		copyFilePath =  copyFilePath.replace("$", ".");
		System.out.println("copyFilePath >>> " + copyFilePath);
		copyFile = new File(copyFilePath); //복사 파일객체를 생성해준다.
		//3-2) 신규 파일로 작성해준다.
		FileOutputStream fosCopyFilePath = new FileOutputStream(copyFilePath); 
		BufferedWriter bw = null;
		bw = new BufferedWriter(new OutputStreamWriter(fosCopyFilePath, "UTF-8")); //인코딩.
		String date = LocalDate.now().toString();

		List<HashMap<String, String>> list = new ArrayList<>();
		HashMap<String, String> map = new HashMap<>();
		map.put("package", lineArr[7]+"."+middlePath);
		map.put("classNm", newClassNm);
		map.put("author", prop.getProperty("author"));
		map.put("date", date);
		map.put("note", "");
		map.put("desc", lineArr[9]);
		map.put("funcDiv", lineArr[1]);
		map.put("API_NM", lineArr[2]);
		list.add(map);

		while((originFileLine = brOrginFile.readLine())!=null){
			//일치하는 패턴에서는 바꿀 문자로 변환. 한 줄에 $${}가 여러 개 있을 수 있으므로 반복해서 읽어주어야 한다.
				while(originFileLine.indexOf("$$") > 0){ //포함하면 반복
			
				if(originFileLine.contains("$${")){ 
				String key = originFileLine.substring(originFileLine.indexOf("{")+1, originFileLine.indexOf("}"));
				String _key = "$${"+key+"}";
				originFileLine = originFileLine.replace(_key, map.get(key));
					} 
				}
			
			bw.write(originFileLine);
			bw.newLine();
		}
		// fisProp.close();
		// brOrginFile.close();
		// fosCopyFilePath.close();
		bw.close();
	}
	br.close();
	}
}
