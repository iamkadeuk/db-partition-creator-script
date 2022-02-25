package main;


public class Main {
	public static void main(String[] args) {
		
		// Parameters ////////////////////////////////
		String requestDate = "2022-3"; // ex: 2022-01 or 2022-1
		String schemaNm = "osown";
		String partitionTableNm = "om_eqp_bas_kep_inf";
		Integer type = 1; //(0: Daily, 1: Hourly)
		//////////////////////////////////////////////
		
		String[] splitDate = requestDate.split("-");
		
		if (Integer.valueOf(splitDate[1]) > 12 || Integer.valueOf(splitDate[0]) < 1000 || Integer.valueOf(splitDate[0]) > 9999) {
			System.out.println("Request Parameters are Wrong");
			System.exit(0);
		}
		
		if (splitDate[1].length() == 1) {
			splitDate[1] = "0" + splitDate[1];
		}
		
		Integer j = 0;
		
		if ("04".equals(splitDate[1]) || "06".equals(splitDate[1]) || "09".equals(splitDate[1]) || "11".equals(splitDate[1])) {
			j = 30;
		} else if ("02".equals(splitDate[1])) {
			j = 28;
		} else {
			j = 31;
		}
		
		if (type == 0) {
			for (Integer i = 1; i <= j; i ++) {
				
				Integer requestYear = Integer.valueOf(splitDate[0]);
				Integer requestMonth = Integer.valueOf(splitDate[1]);
				Integer requestDay = i;
				Integer requestDayPlus = i + 1;

				Integer lastYear = Integer.valueOf(splitDate[0]);
				Integer lastMonth = Integer.valueOf(splitDate[1]) + 1;
				
				System.out.printf("create table " + schemaNm + "." + partitionTableNm + "_" + String.format("%02d", requestYear) + String.format("%02d", requestMonth) + String.format("%02d", requestDay));
				System.out.printf(" partition of " + schemaNm + "." + partitionTableNm + " for values from ('");
				System.out.printf(String.format("%02d", requestYear) + "-" + String.format("%02d", requestMonth) + "-" + String.format("%02d", requestDay) + " 00:00:00') to ('");
				
				
				if ((j == 31 && requestDayPlus == 32) || (j == 30 && requestDayPlus == 31) || (j == 28 && requestDayPlus == 29)) {
					if (lastMonth == 13) {
						lastYear = Integer.valueOf(splitDate[0]) + 1;
						lastMonth = 1;
					}
					System.out.printf(String.format("%02d", lastYear) + "-" + String.format("%02d", lastMonth) + "-" + String.format("%02d", 1) + " 00:00:00')");
					System.out.println(" tablespace pg_default;");
					System.out.println("alter table if exists " + schemaNm + "." + partitionTableNm + "_" + String.format("%02d", requestYear) + String.format("%02d", requestMonth) + String.format("%02d", requestDay) + " owner to " + schemaNm + ";");
				} else {
					System.out.printf(String.format("%02d", requestYear) + "-" + String.format("%02d", requestMonth) + "-" + String.format("%02d", requestDayPlus) + " 00:00:00')");
					System.out.println(" tablespace pg_default;");
					System.out.println("alter table if exists " + schemaNm + "." + partitionTableNm + "_" + String.format("%02d", requestYear) + String.format("%02d", requestMonth) + String.format("%02d", requestDay) + " owner to " + schemaNm + ";");
				}
			}
		} else if (type == 1) {
				


			for (Integer i = 1; i <= j; i ++) {
				
				Integer requestYear = Integer.valueOf(splitDate[0]);
				Integer requestMonth = Integer.valueOf(splitDate[1]);
				Integer requestDay = i;
				Integer requestDayPlus = i + 1;

				Integer lastYear = Integer.valueOf(splitDate[0]);
				Integer lastMonth = Integer.valueOf(splitDate[1]) + 1;
				
//				System.out.printf("create table " + schemaNm + "." + partitionTableNm + "_" + String.format("%02d", requestYear) + String.format("%02d", requestMonth) + String.format("%02d", requestDay));
//				System.out.printf(" partition of " + schemaNm + "." + partitionTableNm + " for values from ('");
//				System.out.printf(String.format("%02d", requestYear) + "-" + String.format("%02d", requestMonth) + "-" + String.format("%02d", requestDay) + " 00:00:00') to ('");
				
				
				if ((j == 31 && requestDayPlus == 32) || (j == 30 && requestDayPlus == 31) || (j == 28 && requestDayPlus == 29)) {
					//말일에만 작동함
					if (lastMonth == 13) {
						lastYear = Integer.valueOf(splitDate[0]) + 1;
						lastMonth = 1;
					}
					
					for (Integer t = 0; t <= 23; t ++) {
						Integer requestTime = t;
						Integer requestTimePlus = t + 1;
						
						if (requestTimePlus == 24) {
							//다음날짜로 변경처리
							System.out.printf("create table " + schemaNm + "." + partitionTableNm + "_" + String.format("%02d", requestYear) + String.format("%02d", requestMonth) + String.format("%02d", requestDay) + String.format("%02d", requestTime));
							System.out.printf(" partition of " + schemaNm + "." + partitionTableNm + " for values from ('");
							System.out.printf(String.format("%02d", requestYear) + "-" + String.format("%02d", requestMonth) + "-" + String.format("%02d", requestDay) + " " + String.format("%02d", requestTime) + ":00:00') to ('");
							
							System.out.printf(String.format("%02d", lastYear) + "-" + String.format("%02d", lastMonth) + "-" + String.format("%02d", 1) + " 00:00:00')");
							System.out.println(" tablespace pg_default;");
							System.out.println("alter table if exists " + schemaNm + "." + partitionTableNm + "_" + String.format("%02d", requestYear) + String.format("%02d", requestMonth) + String.format("%02d", requestDay) + String.format("%02d", requestTime) + " owner to " + schemaNm + ";");
						} else {
							//동일날짜 유지
							System.out.printf("create table " + schemaNm + "." + partitionTableNm + "_" + String.format("%02d", requestYear) + String.format("%02d", requestMonth) + String.format("%02d", requestDay) + String.format("%02d", requestTime));
							System.out.printf(" partition of " + schemaNm + "." + partitionTableNm + " for values from ('");
							System.out.printf(String.format("%02d", requestYear) + "-" + String.format("%02d", requestMonth) + "-" + String.format("%02d", requestDay) + " " + String.format("%02d", requestTime) + ":00:00') to ('");
							
							System.out.printf(String.format("%02d", requestYear) + "-" + String.format("%02d", requestMonth) + "-" + String.format("%02d", requestDay) + " " + String.format("%02d", requestTimePlus) + ":00:00')");
							System.out.println(" tablespace pg_default;");
							System.out.println("alter table if exists " + schemaNm + "." + partitionTableNm + "_" + String.format("%02d", requestYear) + String.format("%02d", requestMonth) + String.format("%02d", requestDay) + String.format("%02d", requestTime) + " owner to " + schemaNm + ";");
						}
					}
					
					
				} else {
					
					
					for (Integer t = 0; t <= 23; t ++) {
						Integer requestTime = t;
						Integer requestTimePlus = t + 1;
						
						if (requestTimePlus == 24) {
							//다음날짜로 변경처리
							System.out.printf("create table " + schemaNm + "." + partitionTableNm + "_" + String.format("%02d", requestYear) + String.format("%02d", requestMonth) + String.format("%02d", requestDay) + String.format("%02d", requestTime));
							System.out.printf(" partition of " + schemaNm + "." + partitionTableNm + " for values from ('");
							System.out.printf(String.format("%02d", requestYear) + "-" + String.format("%02d", requestMonth) + "-" + String.format("%02d", requestDay) + " " + String.format("%02d", requestTime) + ":00:00') to ('");
							
							System.out.printf(String.format("%02d", requestYear) + "-" + String.format("%02d", requestMonth) + "-" + String.format("%02d", requestDayPlus) + " 00:00:00')");
							System.out.println(" tablespace pg_default;");
							System.out.println("alter table if exists " + schemaNm + "." + partitionTableNm + "_" + String.format("%02d", requestYear) + String.format("%02d", requestMonth) + String.format("%02d", requestDay) + String.format("%02d", requestTime) + " owner to " + schemaNm + ";");
						} else {
							//동일날짜 유지
							System.out.printf("create table " + schemaNm + "." + partitionTableNm + "_" + String.format("%02d", requestYear) + String.format("%02d", requestMonth) + String.format("%02d", requestDay) + String.format("%02d", requestTime));
							System.out.printf(" partition of " + schemaNm + "." + partitionTableNm + " for values from ('");
							System.out.printf(String.format("%02d", requestYear) + "-" + String.format("%02d", requestMonth) + "-" + String.format("%02d", requestDay) + " " + String.format("%02d", requestTime) + ":00:00') to ('");
							
							System.out.printf(String.format("%02d", requestYear) + "-" + String.format("%02d", requestMonth) + "-" + String.format("%02d", requestDay) + " " + String.format("%02d", requestTimePlus) + ":00:00')");
							System.out.println(" tablespace pg_default;");
							System.out.println("alter table if exists " + schemaNm + "." + partitionTableNm + "_" + String.format("%02d", requestYear) + String.format("%02d", requestMonth) + String.format("%02d", requestDay) + String.format("%02d", requestTime) + " owner to " + schemaNm + ";");
						}
						
						
						
					}
					
					
				}
			}
			
			
			
		}
	}
}
/*
//일별
CREATE TABLE osown.om_bat_exec_hst_20220224 PARTITION OF 
osown.om_bat_exec_hst FOR VALUES FROM ('2022-02-24 00:00:00') TO ('2022-02-25 00:00:00')
TABLESPACE pg_default;
ALTER TABLE IF EXISTS osown.om_bat_exec_hst_20220224 OWNER to osown;

//시간별
CREATE TABLE osown.om_systm_tree_tn_bas_2022022401 PARTITION OF osown.om_systm_tree_tn_bas
    FOR VALUES FROM ('2022-02-24 01:00:00') TO ('2022-02-24 02:00:00')
TABLESPACE pg_default;
ALTER TABLE IF EXISTS osown.om_systm_tree_tn_bas_2022022401 OWNER to osown;
*/
