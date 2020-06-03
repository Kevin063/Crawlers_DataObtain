
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class HttpRequest {
    /**
     * 向指定 URL 发送POST方法的请求
     * 
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
	static String url="http://icid.iachina.cn/front/viewThird.do";
	static String path="C:\\Users\\liuhengyuan-jk\\Desktop\\Programs\\crawlers\\company link.txt";
	static String outputadd="C:\\Users\\liuhengyuan-jk\\Desktop\\Programs\\crawlers\\ThirdLIst.txt";
	static String magic2=";\">查看合作第三方详情</a></p>";
	static String finalurl="http://icid.iachina.cn/front/viewTerraceProduct.do";
	public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }    
    public static void main(String[] args) {
    	String form;
    	int count=0; //For testing
    	int finished=0; // Finished gotten data
    	try {
    		// Initialize the document flow
			BufferedReader br=new BufferedReader(new FileReader(path));
			FileWriter out=new FileWriter(outputadd);
			BufferedWriter output=new BufferedWriter(out);
			// Read the processed page id and set the param which is going to be sent.
			do{
			form=br.readLine();
			String columnid=form.substring(0,form.indexOf("@"));
			String internetInformationNo=form.substring(form.indexOf("@")+1);
			String param="columnid="+columnid+"&zj=02&internetInformationNo="+internetInformationNo;
			String list=sendPost(url,param);
			System.out.println("Processing:"+param);//report process
			// Wait blocking rule to end, test per 10 min.
			while(list.indexOf("010-66290327")!=-1) {
				System.out.println("已完成抓取数："+finished);
				Thread.sleep(600000);
				list=sendPost(url,param);
			}
			//Clean the page data. Find all the info we need inside.
			if(list.indexOf("历史合作第三方")!=-1) list.substring(0,list.indexOf("历史合作第三方"));
					while(list.indexOf("onclick=\"zjDetail")!=-1) {
						list=list.substring(list.indexOf("onclick=\"zjDetail")+19);
						String code=list.substring(0,list.indexOf("','"));
						String type=list.substring(list.indexOf(magic2)-9,list.indexOf(magic2)-7);
						String comType=list.substring(list.indexOf(magic2)-4,list.indexOf(magic2)-2);
						param="informationno="+internetInformationNo+"&columnid="+columnid+"&terraceNo="+code+"&zj=03&oldTerraceNo="+code+"&internetInformationNo="+internetInformationNo+"&type="+type+"&comType="+comType;
						String finalpage=sendPost(finalurl,param);
						// Wait blocking rule to end, test per 10 min
						while(finalpage.indexOf("010-66290327")!=-1) {
							System.out.println("已完成抓取数："+finished);
							Thread.sleep(600000);
							finalpage=sendPost(finalurl,param);
						}
						//Use the data to send a request, then clean the data and find what we need.
						if(finalpage.indexOf("第三方网络平台全称")+17>0&&finalpage.indexOf("第三方网络平台简称")-17>0&&finalpage.indexOf("第三方网络平台网站地址")+19>0&&finalpage.indexOf("第三方网络平台备案信息")-17>0) 	
						{
							Thread.sleep(5001);//Avoid over-frequent visit
							String finalinfo=finalpage.substring(finalpage.indexOf("第三方网络平台全称")+17,finalpage.indexOf("第三方网络平台简称")-17)+"   "+finalpage.substring(finalpage.indexOf("第三方网络平台网站地址")+19,finalpage.indexOf("第三方网络平台备案信息")-17);
						finished++;
						output.write(finalinfo);
						output.newLine();
						output.flush();	
						}
					}
			count++;
		}while(br!=null);
		
    	} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}