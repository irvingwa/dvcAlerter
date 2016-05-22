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
package coding.cowboys.scrapers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import coding.cowboys.util.ResortWrapper;
import coding.cowboys.util.SiteUrls;

public class DvcByResaleScraper {

	public List<ResortWrapper> findResorts() {
		List<ResortWrapper> wrappers = new ArrayList<ResortWrapper>();
		Document doc = null;
		try {
			doc = Jsoup.connect(SiteUrls.DVC_BY_RESALE).timeout(60000).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (doc != null) {
			for (Element table : doc.select("table[class=currentresales]")) {

				boolean firstTr = true;
				for (Element row : table.select("tr")) {

					Elements tds = row.select("td");
					if (tds.size() == 10) {
						if (!firstTr) {
							String saleStatus = tds.get(8).text();
							if (!saleStatus.equals("Sale Pending")) {
								String url = SiteUrls.DVC_BY_RESALE + " ID: " + tds.get(0).text();
								String resort = tds.get(1).text();
								String points = tds.get(2).text();
								String useYear = tds.get(3).text();
								String pointSummary = tds.get(5).text();
								String pricePerPoint = tds.get(6).text();
								String totalPrice = tds.get(7).text();
								ResortWrapper wrapper = new ResortWrapper();
								wrapper.setPoints(points);
								wrapper.setPointSummary(pointSummary);
								wrapper.setPricePerPoint(pricePerPoint);
								wrapper.setTotalPrice(totalPrice);
								wrapper.setUrl(url);
								wrapper.setUseYear(useYear);
								wrapper.setResort(resort);
								wrappers.add(wrapper);

							}

						} else {
							firstTr = false;
						}
					}
				}

			}
		} else {
			System.out.println("DVC By Resales returned null");
		}
		return wrappers;
	}

	public static void main(String[] args) {
		DvcByResaleScraper test = new DvcByResaleScraper();
		List<ResortWrapper> hello = test.findResorts();
		System.out.println(hello.toString());
	}

}
