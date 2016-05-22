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
package coding.cowboys.util;

import org.apache.commons.lang3.math.NumberUtils;

public class ResortWrapper {

	private String points;
	private String totalPrice;
	private String useYear;
	private String pointSummary;
	private String url;
	private String pricePerPoint;
	private String resort;

	public String getPricePerPoint() {
		return pricePerPoint;
	}

	public void setPricePerPoint(String pricePerPoint) {
		this.pricePerPoint = pricePerPoint;
	}

	public Integer getPoints() {
		if(!NumberUtils.isNumber(totalPrice)){
			System.out.println("We had a problem here need to log it: " + toString());
			return 0;
		}
		return Integer.valueOf(points);
	}

	public void setPoints(String points) {
		points = points.replace("$", "").replace(",", "").replace("POINTS", "");
		this.points = points;
	}

	public Double getTotalPrice() {
		if(!NumberUtils.isNumber(totalPrice)){
			System.out.println("We had a problem here need to log it: " + toString());
			return 100000000d;
		}
		return Double.valueOf(totalPrice);
	}

	public void setTotalPrice(String totalPrice) {
		totalPrice = totalPrice.replaceAll("$", "").replace(",", "").replace("$", "");
		this.totalPrice = totalPrice;
	}

	public String getUseYear() {
		return useYear;
	}

	public void setUseYear(String useYear) {
		this.useYear = useYear;
	}

	public String getPointSummary() {
		return pointSummary;
	}

	public void setPointSummary(String pointSummary) {
		this.pointSummary = pointSummary;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}	


	public String getResort() {
		return resort;
	}

	public void setResort(String resort) {
		this.resort = resort;
	}
	
	@Override
	public String toString() {
		String retString = "\n------Start Listing------" + "\n" + "Resort: " + resort + "\n" + "URL: " + url + "\n" + "Points: " + points + "\n" + "Price Per Point: $" + pricePerPoint + "\n" + "Total Price: $" + totalPrice + "\n" + "Point Summary: " + pointSummary + "\n"+"------End Listing------\n";
		return retString;
	}

}
