package com.auction.bootstraper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.auction.cachecontroller.AuctionCacheController;
import com.auction.jetty.AuctionJettyServer;
import com.auction.pojos.AuctionItemPojo;

public class AuctionBootsraper {
	
	 private static Logger logger = LogManager.getLogger(AuctionBootsraper.class);
	public static void main(String[] args) {
		try {
			
			logger.info("@@@@@@@@Auction BootStraper Started@@@@@@@@@@@@@");
			
			
//			readItemCodeExcel();
//			readPropertyFile();
			AuctionJettyServer.startJetty("localhost","3450");
			
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("exception",e);
			e.printStackTrace();
		}
		
	}
	
	private static void readItemCodeExcel() {
		XSSFWorkbook workbook =null;
		FileInputStream fileInputStream=null;
		try {
			fileInputStream = new FileInputStream(new File("../configuration/ItemCode.xlsx"));
			workbook= new XSSFWorkbook(fileInputStream);
			// workbook.setMissingCellPolicy(Row.MissingCell);
			XSSFSheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			Row row;// rowIterator.next();
			Cell cell;
			String itemCode;
			String stepRate;
			String basePrice;
			rowIterator.next();
			while (rowIterator.hasNext()) {
				row = rowIterator.next();
				row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
				row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
				row.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
				itemCode = row.getCell(0).getStringCellValue().trim();
				stepRate = row.getCell(1).getStringCellValue().trim();
				basePrice = row.getCell(2).getStringCellValue().trim();
				
			AuctionCacheController.getRunningAuctionsItemsMap().put(itemCode.trim(), new AuctionItemPojo(itemCode.trim(), Integer.parseInt(stepRate.trim()), Integer.parseInt(basePrice.trim()), true));

			
		}
			logger.info("Running Auction Map.."+AuctionCacheController.getRunningAuctionsItemsMap());
			} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error("exception",e);
		}
		finally {
			if (fileInputStream!=null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
		
		
		
	}
	public static void readPropertyFile() {
		
		try {
			 FileReader reader=new FileReader("../configuration/server.properties");  
		      
			    Properties p=new Properties();  
			    p.load(reader);  
			    String ip=p.getProperty("ip");
			    String port=p.getProperty("port");
			    
			    
			    AuctionCacheController.setIp(ip);
			    AuctionCacheController.setPort(port);
			
			    logger.info("Ip:"+ip+"-----Port:"+port);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error("exception",e);
		}
		
	}

}
