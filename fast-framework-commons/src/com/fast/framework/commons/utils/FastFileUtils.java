package com.fast.framework.commons.utils;
//package com.dyxnet.wm.fastui.utils;
//
//import java.io.File;
//import java.net.URLDecoder;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.commons.io.FileUtils;
//
///**
// * 文件处理工具类
// * @author lionchen
// *
// */
//public class FastFileUtils extends FileUtils{
//
//	/**
//	 * 解码路径（针对中文路径问题）
//	 * @param path
//	 * @return
//	 */
//	@SuppressWarnings("deprecation")
//	public static String decodePath(String path){
//		
//		if(path != null){
//			return URLDecoder.decode(path);
//		}
//		
//		return path;
//	}
//	
//	/**
//	 * 指定文件名获取文件列表
//	 * @param path
//	 * @param suffix
//	 * @return
//	 */
//	public static List<File> getFileList(String path, final String suffix){
//		
//		List<File> r = new ArrayList<File>();
//		File f = new File(path);
//		if(f.isDirectory()){
//			File[] listFiles = f.listFiles();
//			for(File tempFile : listFiles){
//				String name = tempFile.getName();
//				if(tempFile.isDirectory()){
//					r.addAll(getFileList(tempFile.getAbsolutePath(), suffix));
//				}else{
//					if(name.matches(suffix)){
//						r.add(tempFile);
//					}
//				}
//			}
//		}else{
//			String name = f.getName();
//			if(name.matches(suffix)){
//				r.add(f);
//			}
//		}
//		
//		return r;
//	}
//	
//}
