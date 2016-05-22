/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package coding.cowboys.webservice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Singleton;
import javax.ejb.Startup;

import coding.cowboys.scrapers.DvcByResaleScraper;
import coding.cowboys.scrapers.DvcMagicResalesScraper;
import coding.cowboys.scrapers.DvcResaleMarketScraper;
import coding.cowboys.scrapers.DvcResalesScraper;
import coding.cowboys.scrapers.ResalesDvcScraper;
import coding.cowboys.scrapers.SaratogaFidelityScraper;
import coding.cowboys.util.EmailDvcAlert;
import coding.cowboys.util.ResortWrapper;

@Startup
@Singleton
public class DVCAlertStartup {
	
	private static final int MIN_NUM_POINTS = 100;
	private static final int TOTAL_PRICE = 8500;
	//This is in milliseconds Default is 12 Hours
	private static final int TIME_INTERVAL = 43200000;

	public DVCAlertStartup() {
		SaratogaFidelityScraper saratogaFidelityScraper = new SaratogaFidelityScraper();
		DvcByResaleScraper dvcByResale = new DvcByResaleScraper();
		ResalesDvcScraper resalesDvcScraper = new ResalesDvcScraper();
		DvcResalesScraper dvcResales = new DvcResalesScraper();
		DvcMagicResalesScraper magicResales = new DvcMagicResalesScraper();
		DvcResaleMarketScraper reslaseMarket = new DvcResaleMarketScraper();
		Map<String, ResortWrapper> emailMap = new HashMap<String, ResortWrapper>();
		Date startDate = Calendar.getInstance().getTime();
		while (true) {
			try {
				List<ResortWrapper> passedResorts = new ArrayList<ResortWrapper>();
				// Look at saratoga fidelity
				List<ResortWrapper> resorts = saratogaFidelityScraper.findResorts();
				// add dvc by resale
				resorts.addAll(dvcByResale.findResorts());
				// add resales dvc
				resorts.addAll(resalesDvcScraper.findResorts());
				//add dvc resales
				resorts.addAll(dvcResales.findResorts());
				//add dvc Magic Resales
				resorts.addAll(magicResales.findResorts());
				//add dvc resales market
				resorts.addAll(reslaseMarket.findResorts());
				for (ResortWrapper resortWrapper : resorts) {

					if (resortWrapper.getPoints() >= MIN_NUM_POINTS) {
						if (resortWrapper.getTotalPrice() <= TOTAL_PRICE) {
							if (!emailMap.containsKey(resortWrapper.getUrl())) {
								passedResorts.add(resortWrapper);
								emailMap.put(resortWrapper.getUrl(), resortWrapper);
							}
						}
					}
				}

				if (!passedResorts.isEmpty()) {
					String body = "";
					for (ResortWrapper resortWrapper : passedResorts) {
						body = body + resortWrapper.toString();
						
					}
					EmailDvcAlert.sendFromGMail(body);
				}

				Date currTime = Calendar.getInstance().getTime();
				Calendar startCal = Calendar.getInstance();
				startCal.setTime(startDate);
				startCal.add(Calendar.DATE, 1);

				if (currTime.after(startCal.getTime())) {
					startDate = Calendar.getInstance().getTime();
					if (emailMap.isEmpty()) {
						EmailDvcAlert
								.sendFromGMail("Sorry we found no matches for you today.... but we are still looking!");
					}
					emailMap.clear();
				}
				try {
					Thread.sleep(TIME_INTERVAL);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (Throwable t) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				t.printStackTrace(pw);
				String body = sw.toString();
				EmailDvcAlert.sendFromGMail(body);
			}
		}
	}

	public static void main(String[] args) {
		new DVCAlertStartup();
	}

}
