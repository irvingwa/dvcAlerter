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

import javax.print.attribute.standard.NumberUp;

import org.apache.commons.lang3.math.NumberUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import coding.cowboys.util.ResortWrapper;
import coding.cowboys.util.SiteUrls;

public class DvcMagicResalesScraper {

	public List<ResortWrapper> findResorts() {
		List<ResortWrapper> wrappers = new ArrayList<ResortWrapper>();
		Document doc = null;
		try {
			doc = Jsoup.connect(SiteUrls.DVC_MAGIC_RESALES).timeout(60000).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (doc != null) {
			for (Element element : doc.select("table#listALL")) {
				for (Element row : element.select("tr")) {
					if (row.hasClass("stat-Active")) {
						ResortWrapper wrapper = new ResortWrapper();
						Elements data = row.select("td");
						wrapper.setResort(getResortFromText(data.get(0).text()));
						wrapper.setUseYear(data.get(1).text());
						wrapper.setPoints(data.get(2).text());
						wrapper.setPricePerPoint(data.get(3).text());
						wrapper.setTotalPrice(data.get(4).text());
						wrapper.setPointSummary(data.get(0).text().replace(wrapper.getResort(), ""));
						wrapper.setUrl("http://www.dvcmagicresales.com/dvcmr/resales-all-listings/");
						wrappers.add(wrapper);
					}

				}

			}

		} else {
			System.out.println("DVC Magic Resales returned null");
		}
		return wrappers;
	}

	/**
	 * getResortFromText - Finds the resort in the text and returns it.
	 * 
	 * @param text
	 * @return
	 */
	//TODO: Find a better way to do this
	private String getResortFromText(String text) {
		String retVal = "";
		if(text.contains("Animal Kingdom")){
			retVal = "Animal Kingdom";
		}else if(text.contains("Aulani")){
			retVal = "Aulani";
		}else if(text.contains("Bay Lake Tower")){
			retVal = "Bay Lake Tower";
		}else if(text.contains("Beach Club")){
			retVal = "Beach Club";
		}else if(text.contains("Boardwalk")){
			retVal = "Boardwalk";
		}else if(text.contains("Grand Californian")){
			retVal = "Grand Californian";
		}else if(text.contains("Grand Floridian")){
			retVal = "Grand Floridian";
		}else if(text.contains("Hilton Head")){
			retVal = "Hilton Head";
		}else if(text.contains("Old Key West")){
			retVal = "Old Key West";
		}else if(text.contains("Saratoga Springs")){
			retVal = "Saratoga Springs";
		}else if(text.contains("Vero Beach")){
			retVal = "Vero Beach";
		}else if(text.contains("Wilderness Lodge")){
			retVal = "Wilderness Lodge";
		}else if(text.contains("Polynesian")){
			retVal = "Polynesian";
		}
		return retVal;
	}

	public static void main(String[] args) {
		DvcMagicResalesScraper test = new DvcMagicResalesScraper();
		List<ResortWrapper> hello = test.findResorts();
		System.out.println(hello.toString());
	}

}
