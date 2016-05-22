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

public class DvcResaleMarketScraper {

	public List<ResortWrapper> findResorts() {
		List<ResortWrapper> wrappers = new ArrayList<ResortWrapper>();
		Document doc = null;
		try {
			doc = Jsoup.connect(SiteUrls.DVC_RESALE_MARKET).timeout(60000).userAgent("Mozilla").get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (doc != null) {
			for (Element table : doc.select("table[class=dvc-listings-table tablesorter]")) {

				for (Element row : table.select("tr[class=dvc-listings-row]")) {

					Elements tds = row.select("td");
					if (tds.size() == 19) {
						String pending = tds.get(10).text();
						if(!pending.equalsIgnoreCase("Offer Accepted") && !pending.equalsIgnoreCase("Sale Pending")){
								ResortWrapper wrapper = new ResortWrapper();
								if(tds.get(1).text().split("-").length == 2){
								wrapper.setUrl("http://www.dvcresalemarket.com/listing/" + tds.get(1).text().split("-")[0].toLowerCase());
								}else{
									wrapper.setUrl(tds.get(1).text());
								}
								wrapper.setPoints(tds.get(12).text());
								wrapper.setPointSummary(tds.get(0).text());
								wrapper.setPricePerPoint(tds.get(8).text().replace("$", ""));
								wrapper.setResort(tds.get(11).text());
								wrapper.setTotalPrice(tds.get(17).text());
								wrapper.setUseYear(tds.get(13).text());
								wrappers.add(wrapper);
						}
					}
				}
			}
		} else {
			System.out.println("DVC Resale Market returned null");
		}
		return wrappers;

	}

	public static void main(String[] args) {
		DvcResaleMarketScraper test = new DvcResaleMarketScraper();
		List<ResortWrapper> hello = test.findResorts();
		System.out.println(hello.toString());
	}

}
