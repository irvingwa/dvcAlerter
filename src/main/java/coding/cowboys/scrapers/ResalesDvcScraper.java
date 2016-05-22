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

public class ResalesDvcScraper {

	public List<ResortWrapper> findResorts() {
		List<ResortWrapper> wrappers = new ArrayList<ResortWrapper>();
		Document doc = null;
		try {
			doc = Jsoup.connect(SiteUrls.RESALES_DVC).timeout(60000).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (doc != null) {
			for (Element table : doc.select("table#tablepress-14")) {
				System.out.println("1");
				boolean firstTr = true;
				for (Element row : table.select("tr")) {

					Elements tds = row.select("td");
					if (tds.size() == 7) {
						if (!firstTr) {
							String saleStatus = tds.get(6).text();
							if (!saleStatus.equalsIgnoreCase("Sale pending")) {
								String url = SiteUrls.RESALES_DVC + " ID: " + tds.get(0).text();
								String resort = tds.get(1).text();
								String points = tds.get(2).text();
								String pricePerPoint = tds.get(3).text().replace("$", "");
								String pointSummary = tds.get(5).text();
								String totalPrice = tds.get(6).text().replace("$", "");
								ResortWrapper wrapper = new ResortWrapper();
								wrapper.setPoints(points);
								wrapper.setPointSummary(pointSummary);
								wrapper.setPricePerPoint(pricePerPoint);
								wrapper.setTotalPrice(totalPrice);
								wrapper.setUrl(url);
								wrapper.setResort(resort);
								if (!wrapper.getResort().equals("RESORT")) {
									wrappers.add(wrapper);

								}

							}
						} else {
							firstTr = false;
						}
					}

				}
			}
		} else {
			System.out.println("Resales DVC returned null");
		}
		return wrappers;
	}

	public static void main(String[] args) {
		ResalesDvcScraper test = new ResalesDvcScraper();
		List<ResortWrapper> hello = test.findResorts();
		System.out.println(hello.toString());
	}

}
