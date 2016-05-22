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

public class DvcResalesScraper {

	public List<ResortWrapper> findResorts() {
		List<ResortWrapper> wrappers = new ArrayList<ResortWrapper>();
		Document doc = null;
		try {
			doc = Jsoup.connect(SiteUrls.DVC_RESALES).timeout(60000).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (doc != null) {
			for (Element table : doc.select("table")) {
				boolean firstTr = true;
				boolean addDescrip = false;
				ResortWrapper wrapper = new ResortWrapper();
				for (Element row : table.select("tr")) {
					if (addDescrip) {
						wrapper.setPointSummary(row.text());
						wrappers.add(wrapper);
						addDescrip = false;
					} else {
						wrapper = new ResortWrapper();
					}

					Elements tds = row.select("td");
					if (tds.size() == 8) {
						if (!firstTr) {
							String url = SiteUrls.DVC_RESALES + " ID: " + tds.get(0).text();
							String resortAndPricePerPoint = tds.get(1).text();
							String[] resortPointArray = resortAndPricePerPoint.split("-");
							String resort = resortPointArray[0];
							String pricePerPoint = resortPointArray[1];

							if (!pricePerPoint.trim().equalsIgnoreCase("Sale Pending")
									&& !pricePerPoint.trim().equalsIgnoreCase("Sale Pending.")) {
								String points = tds.get(2).text().replaceAll("$", "").replaceAll("/pt.", "")
										.replaceAll("/pt", "");
								String useYear = tds.get(3).text();
								String totalPrice = tds.get(7).text().replaceAll("$", "");
								if (totalPrice.startsWith("$")) {
									totalPrice = totalPrice.substring(1, totalPrice.length());
								}
								wrapper.setPoints(points);
								wrapper.setUseYear(useYear);
								wrapper.setPricePerPoint(
										pricePerPoint.replace("$", "").replaceAll("/pt.", "").replaceAll("/pt", ""));
								wrapper.setTotalPrice(totalPrice.replaceAll("$", ""));
								wrapper.setUrl(url);
								wrapper.setResort(resort);
								addDescrip = true;
							}
						} else {
							firstTr = false;
						}
					}

				}
			}
		}else{
			System.out.println("DVC Resales returned null");
		}
		return wrappers;
	}

	public static void main(String[] args) {
		DvcResalesScraper test = new DvcResalesScraper();
		List<ResortWrapper> hello = test.findResorts();
		System.out.println(hello.toString());
	}

}
