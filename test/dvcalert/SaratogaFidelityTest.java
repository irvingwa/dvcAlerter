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
package dvcalert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import coding.cowboys.util.ResortWrapper;
import coding.cowboys.util.SiteUrls;

public class SaratogaFidelityTest {

	public SaratogaFidelityTest() {

		Document doc = null;
		try {
			doc = Jsoup.connect(SiteUrls.SARATOGA_FIDELITY).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (Element table : doc.select("table[class=views-table cols-7]")) {
			List<ResortWrapper> wrappers = new ArrayList<ResortWrapper>();
			for (Element row : table.select("tr")) {
				Elements tds = row.select("td");
				if (tds.size() == 7) {
					String pricePerPoint = tds.get(1).text().replace("Price Per Point: ", "");
					if (pricePerPoint.contains("Sale Pending")) {
						pricePerPoint.replace("Sale Pending", "");
					}
					String points = tds.get(2).text().replace("Points: ", "");
					String totalPrice = tds.get(3).text().replace("Total Price: ", "");
					String useYear = tds.get(4).text().replace("Use Year: ", "");
					String pointSummary = tds.get(5).text().replace("Point Summary: ", "");
					String url = tds.get(6).html().replace("<a href=\"", "")
							.replace("\" class=\"btn-small\">View Ad</a>", "");

					ResortWrapper resort = new ResortWrapper();

					resort.setPoints(points);
					resort.setTotalPrice(totalPrice);
					resort.setUseYear(useYear);
					resort.setPointSummary(pointSummary);
					resort.setUrl(url);
					
					wrappers.add(resort);

				}

			}
			System.out.println(wrappers.toString());
		}
		

	}

	public static void main(String[] args) {
		new SaratogaFidelityTest();

	}

}
