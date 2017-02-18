package main;

import processing.core.PApplet;
import processing.core.PFont;

class DrawGraph {

	// Code used for viewing graphs
	public int view = 1; // this variable is used to know in which tap is
							// selected.
	public float[] tabLeft, tabRight;
	public float tabTop, tabBottom;
	public float tabPad = 20;
	public PFont f2, fbold, fTags;
	public int frameX1 = 120, frameY1 = 20, frameX2 = 1220, frameY2 = 650;
	public int labelX = frameX1 + 100, labelY = frameY2 - 30;
	public int plotX1 = frameX1 + 80, plotY1 = frameY1 + 100,
			plotX2 = frameX2 - 50, plotY2 = frameY2 - 70;
	public int maxSightPopulation = 20000, minSightPopulation = 0;
	public int maxSightYear = 3300, minSightYear = 0;
	public int maxSightAirport = 28000, minSightAirport = 0;
	public int maxSightMilitary = 13000, minSightMilitary = 0;
	public int maxSightMonth = 12000, minSightMonth = 0;
	public int maxSightDay = 17796, minSightDay = 0;
	public int maxSightHour = 13900, minSightHour = 0;
	public Integrator[] graphValues = new Integrator[50];
	public int[] populationSightings = { 13676, 19764, 15264, 14184, 12516,
			9112, 5880, 3212, 1348, 1784, 1592, 1992, 388, 364, 572, 344, 636,
			676, 864, 196, 32, 48, 32, 0, 8, 92, 1464, 0, 56, 8, 0, 0, 0, 0, 0,
			0, 44, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 44, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0 };
	public int[] airportSightings = { 21512, 27844, 12392, 9220, 6964, 5036,
			4552, 2872, 2840, 2340, 2020, 1568, 1392, 912, 632, 604, 456, 624,
			168, 416, 152, 260, 104, 64, 72, 28, 104, 64, 84, 180, 132, 16, 52,
			24, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	public int[] militarySightings = { 5508, 12916, 9444, 6732, 6300, 6104,
			4300, 4436, 3176, 3616, 2784, 3256, 2052, 1308, 1476, 2560, 1780,
			1204, 1252, 1524, 964, 1184, 1040, 1044, 960, 1800, 572, 744, 388,
			280, 268, 208, 276, 160, 152, 148, 116, 396, 284, 192, 380, 488,
			296, 504, 904, 200, 440, 156, 144, 88 };

	public int[] yearSightings = { 4160, 4018, 4800, 5202, 5138, 4600, 5662,
			6407, 5801, 5634 };
	public int[] monthSightings = { 7096, 5832, 6532, 6396, 6096, 7760, 10572,
			9840, 9432, 9652, 8508, 6480 };
	public int[] weekSightings = { 11928, 8884, 8820, 9912, 9676, 12180,
			13796};
	public int[] hourSightings = { 5492, 4216, 3020, 2652, 2336, 2636, 1868,
			1336, 1188, 1496, 1700, 1796, 2160, 1844, 1968, 2052, 2308, 3592,
			5208, 7984, 10992, 13828, 13352, 10168 };

	public Integrator[] graphValues2 = new Integrator[104];
	public Integrator[] graphValues3 = new Integrator[24];
	public PApplet parent;
	public float maxY = 30000;
	
	public DrawGraph(PApplet p) {
		parent = p;
		for (int i = 0; i < 50; i++) {
			graphValues[i] = new Integrator();
		}
		for (int i = 0; i < 104; i++) {
			graphValues2[i] = new Integrator();
		}
		for (int i = 0; i < 24; i++) {
			graphValues3[i] = new Integrator();
		}
		for (int i = 0; i < 104; i++) {
			graphValues2[i].set(0);
			graphValues2[i].target(populationSightings[i]);
		}
	}

	public void draw() {
		parent.strokeWeight(1);
		parent.stroke(0);
		parent.fill(150, 150, 150);
		parent.rect(120, 20, 1160, 630);
		parent.fill(255);
		parent.textAlign(PApplet.LEFT);
		parent.textSize(16);
		drawViewTabs();
		drawAxisLabels();
		drawYLabels();
		drawXLabels();
		if (view == 1) {
			parent.beginShape();
			float x = 0, y = 0;
			parent.fill(255, 255, 255,220);
			parent.noStroke();
			for (int i = 0; i < 40; i++) {
				x = PApplet.map(i, 0, 40, plotX1, plotX2);
				y = PApplet.map(graphValues2[i].value, 0, maxY,
						plotY2, plotY1);
				graphValues2[i].update();

				if (i == 0)
					parent.vertex(plotX1, plotY2);

				parent.vertex(x, y);
				parent.smooth();
			}
			parent.vertex(x, y);
			parent.vertex(plotX2, plotY2);
			parent.endShape();
			parent.strokeWeight(1);
		}
		if (view == 2) {
			parent.beginShape();
			float x = 0, y = 0;
			parent.fill(255, 255, 255,220);
			parent.noStroke();
			for (int i = 0; i < 50; i++) {
				x = PApplet.map(i, 0, 49, plotX1, plotX2);
				y = PApplet.map(graphValues[i].value, 0, maxY,
						plotY2, plotY1);
				graphValues[i].update();
				if (i == 0)
					parent.vertex(plotX1, plotY2);

				parent.vertex(x, y);
			}
			parent.vertex(x, y);
			parent.vertex(plotX2, plotY2);
			parent.endShape();
			parent.strokeWeight(1);
		}
		if (view == 3) {
			parent.beginShape();
			float x = 0, y = 0;
			parent.fill(255, 255, 255,220);
			parent.noStroke();
			for (int i = 0; i < 50; i++) {

				x = PApplet.map(i, 0, 49, plotX1, plotX2);
				y = PApplet.map(graphValues[i].value, 0, maxY,
						plotY2, plotY1);
				graphValues[i].update();
				if (i == 0)
					parent.vertex(plotX1, plotY2);

				parent.vertex(x, y);
			}
			parent.vertex(x, y);
			parent.vertex(plotX2, plotY2);
			parent.endShape();
			parent.strokeWeight(1);
		}
		if (view == 4) {
			parent.beginShape();
			float x = 0, y = 0;
			parent.fill(255, 255, 255,220);
			parent.noStroke();
			for (int i = 0; i < 10; i++) {
				x = PApplet.map(i, 0, 9, plotX1, plotX2);
				y = PApplet.map(graphValues3[i].value, 0, maxY, plotY2,
						plotY1);
				graphValues3[i].update();
				if (i == 0)
					parent.vertex(plotX1, plotY2);

				parent.vertex(x, y);
				parent.smooth();
			}
			parent.vertex(x, y);
			parent.vertex(plotX2, plotY2);
			parent.endShape();
			parent.strokeWeight(1);
		}
		if (view == 5) {
			parent.beginShape();
			float x = 0, y = 0;
			parent.fill(255, 255, 255,220);
			parent.noStroke();
			for (int i = 0; i < 12; i++) {
				x = PApplet.map(i, 0, 11, plotX1, plotX2);
				y = PApplet.map(graphValues3[i].value, 0, maxY,
						plotY2, plotY1);
				graphValues3[i].update();
				if (i == 0)
					parent.vertex(plotX1, plotY2);

				parent.vertex(x, y);
				parent.smooth();
			}
			parent.vertex(x, y);
			parent.vertex(plotX2, plotY2);
			parent.endShape();
			parent.strokeWeight(1);
		}
		if (view == 6) {
			parent.beginShape();
			float x = 0, y = 0;
			parent.fill(255, 255, 255,220);
			parent.noStroke();
			for (int i = 0; i < 7; i++) {
				x = PApplet.map(i, 0, 6, plotX1, plotX2);
				y = PApplet.map(graphValues3[i].value, 0, maxY, plotY2,
						plotY1);
				graphValues3[i].update();
				if (i == 0)
					parent.vertex(plotX1, plotY2);

				parent.vertex(x, y);
				parent.smooth();
			}
			parent.vertex(x, y);
			parent.vertex(plotX2, plotY2);
			parent.endShape();
			parent.strokeWeight(1);
		}
		if (view == 7) {
			parent.beginShape();
			float x = 0, y = 0;
			parent.fill(255, 255, 255,220);
			parent.noStroke();
			for (int i = 0; i < 24; i++) {

				x = PApplet.map(i, 0, 23, plotX1, plotX2);
				y = PApplet.map(graphValues3[i].value, 0, maxY, plotY2,
						plotY1);
				graphValues3[i].update();
				if (i == 0)
					parent.vertex(plotX1, plotY2);

				parent.vertex(x, y);
				parent.smooth();
			}
			parent.vertex(x, y);
			parent.vertex(plotX2, plotY2);
			parent.endShape();
			parent.strokeWeight(1);
		}
	}

	public void drawViewTabs() {
		if (tabLeft == null) {
			tabLeft = new float[7];
			tabRight = new float[7];
		}
		float runningX = frameX1 + 10;
		tabTop = frameY1 + 10;
		tabBottom = frameY1 + 40;
		parent.textSize(14);
		for (int i = 1; i < 8; i++) {
			String title = "";
			switch (i) {
			case 1:
				title = "Population Density";
				break;
			case 4:
				title = "Year";
				break;
			case 2:
				title = "Distance from Airports";
				break;
			case 3:
				title = "Distance from Military Area";
				break;
			case 5:
				title = "Month";
				break;
			case 6:
				title = "Day";
				break;
			case 7:
				title = "Hour";
				break;
			}

			tabLeft[i - 1] = runningX;
			float titleWidth = parent.textWidth(title);
			tabRight[i - 1] = tabLeft[i - 1] + tabPad + titleWidth + tabPad;

			if (i == view) {
				parent.stroke(0, 0, 0);
				parent.fill(0, 0, 0);
			} else {
				parent.stroke(0, 0, 0);
				parent.noFill();
			}
			parent.rectMode(PApplet.CORNERS);
			parent.rect(tabLeft[i - 1], tabTop, tabRight[i - 1], tabBottom);

			if (i == view) {
				parent.fill(255, 255, 255);
			} else {
				parent.fill(0, 0, 0);
			}
			parent.text(title, runningX + tabPad, frameY1 + 30);

			runningX = tabRight[i - 1];
		}
		parent.rectMode(PApplet.CORNER);
		parent.strokeWeight(1);
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// This method titles the axes of the graphs according to which view the
	// user is using.
	void drawAxisLabels() {
		parent.fill(255);
		parent.textSize(14);
		parent.textLeading(18);
		parent.textAlign(PApplet.CENTER);
		parent.text("Number of Sightings", labelX, plotY1 - 20);
		parent.textAlign(PApplet.CENTER, PApplet.CENTER);

		if (view == 1) {
			parent.text("Population Densities(Individuals per square km)",
					(plotX1 + plotX2) / 2, labelY);
		} else {
			if (view == 4) {
				parent.text("Year", (plotX1 + plotX2) / 2, labelY);

			} else {
				if (view == 2)
					parent.text(
							"Distance From Nearest Airport (In kilometers)",
							(plotX1 + plotX2) / 2, labelY);
				else {
					if (view == 3) {
						parent.text(
								"Distance From Nearest Military Area (In kilometers)",
								(plotX1 + plotX2) / 2, labelY);

					}
					if (view == 5) {
						parent.text(
								"Month (Total sightings in each month across all years)",
								(plotX1 + plotX2) / 2, labelY);

					}
					if (view == 6) {
						parent.text(
								"Day (Total Sightings on each day of the week across all years)",
								(plotX1 + plotX2) / 2, labelY);

					}
					if (view == 7) {
						parent.text(
								"Hour (Total Sightings in each hour of the day across all years)",
								(plotX1 + plotX2) / 2, labelY);

					}

				}

			}
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// The following method draws the X-axis labels according to which view the
	// user is in.
	void drawXLabels() {
		parent.stroke(0);
		parent.strokeWeight((float) 2);
		parent.line(plotX1 - 2, plotY2 + 1, plotX2 - 2, plotY2 + 1);
		parent.strokeWeight(1);
		parent.fill(0);
		parent.textAlign(PApplet.CENTER);
		if (view == 1) {
			for (int i = 0; i <= 10000; i = i + 1000) {
				float x = PApplet.map(i, 0, 10000, plotX1, plotX2);
				parent.text(formatIntegerThousand(i), x, plotY2 + 20);
				parent.strokeWeight((float) 0.15);
				parent.line(x, plotY1, x, plotY2);
			}
		} else {
			if (view == 2) {
				for (int i = 0; i <= 490; i = i + 10) {
					if (i % 50 == 0) {
						float x = PApplet.map(i, 0, 490, plotX1, plotX2);
						parent.text(formatIntegerThousand(i), x, plotY2 + 20);
						parent.strokeWeight((float) 0.15);
						parent.line(x, plotY1, x, plotY2);
					}
				}
			} else {
				if (view == 3) {
					for (int i = 0; i <= 490; i = i + 10) {
						if (i % 50 == 0) {
							float x = PApplet.map(i, 0, 490, plotX1, plotX2);
							parent.text(formatIntegerThousand(i), x,
									plotY2 + 20);
							parent.strokeWeight((float) 0.15);
							parent.line(x, plotY1, x, plotY2);
						}
					}
				} else {
					if (view == 4) {
						for (int i = 1; i <= 10; i++) {
							float x = PApplet.map(i, 1, 10, plotX1, plotX2);
							parent.text(formatIntegerThousand(i + 2000), x,
									plotY2 + 20);
							parent.strokeWeight((float) 0.15);
							parent.line(x, plotY1, x, plotY2);
						}
					}
					if (view == 5) {
						parent.strokeWeight((float) 0.15);
						String startMonth = "Jan";
						for (int i = 0; i < 12; i++) {
							float x = PApplet.map(i, 0, 11, plotX1, plotX2);
							parent.text(startMonth, x, plotY2 + 18);
							parent.line(x, plotY1, x, plotY2);
							if (startMonth.equals("Jan")) {
								startMonth = "Feb";
							} else {
								if (startMonth.equals("Feb")) {
									startMonth = "Mar";
								} else {
									if (startMonth.equals("Mar")) {
										startMonth = "Apr";
									} else {
										if (startMonth.equals("Apr")) {
											startMonth = "May";
										} else {
											if (startMonth.equals("May")) {
												startMonth = "Jun";
											} else {
												if (startMonth.equals("Jun")) {
													startMonth = "Jul";
												} else {
													if (startMonth
															.equals("Jul")) {
														startMonth = "Aug";
													} else {
														if (startMonth
																.equals("Aug")) {
															startMonth = "Sep";
														} else {

															if (startMonth
																	.equals("Sep")) {
																startMonth = "Oct";
															} else {
																if (startMonth
																		.equals("Oct")) {
																	startMonth = "Nov";
																} else {
																	if (startMonth
																			.equals("Nov")) {
																		startMonth = "Dec";
																	}
																}

															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		if (view == 6) {
			String startDay = "Sun";
			for (int i = 1; i < 8; i++) {
				parent.strokeWeight((float) 0.15);
				float x = PApplet.map(i, 1, 7, plotX1, plotX2);
				parent.text(startDay, x, plotY2 + 18);
				parent.line(x, plotY1, x, plotY2);
				startDay = updateNextDay(startDay);
			}
		}
		if (view == 7) {
			for (int i = 0; i < 24; i++) {
				parent.strokeWeight((float) 0.15);
				float x = PApplet.map(i, 0, 23, plotX1, plotX2);
				parent.text(i + "", x, plotY2 + 18);
				parent.line(x, plotY1, x, plotY2);
			}
		}

	}

	void drawYLabels() {
		parent.stroke(0);
		parent.fill(0);
		parent.strokeWeight((float) 2);
		parent.line(plotX1 - 2, plotY1 + 1, plotX1 - 2, plotY2 + 1);
		parent.textAlign(PApplet.RIGHT);
		if (view == 1) {
			for (int i = 5000; i <= maxY; i = i + 5000) {
				float y = PApplet.map(i, 0, 30000, plotY2,
						plotY1);
				parent.text(formatIntegerThousand(i), plotX1 - 10, y + 5);
				parent.strokeWeight((float) 0.5);
				parent.line(plotX1, y, plotX2, y);
			
			}
		} else {
			if (view == 4) {
				for (int i = 5000; i <= maxY; i += 5000) {
					float y = PApplet.map(i, 0, 30000, plotY2, plotY1);
					parent.text(formatIntegerThousand(i), plotX1 - 10, y + 5);
					parent.strokeWeight((float) 0.5);
					parent.line(plotX1, y, plotX2, y);
				}
			} else {
				if (view == 2) {
					for (int i = 5000; i <= maxY; i += 5000) {
						float y = PApplet.map(i, 0, 30000, plotY2,
								plotY1);
						parent.text(formatIntegerThousand(i), plotX1 - 10,
								y + 5);
						parent.strokeWeight((float) 0.5);
						parent.line(plotX1, y, plotX2, y);
					}
				} else {
					if (view == 3) {
						for (int i = 5000; i <= maxY; i += 5000) {
							float y = PApplet.map(i, 0, maxY,
									plotY2, plotY1);
							parent.text(formatIntegerThousand(i), plotX1 - 10,
									y + 5);
							parent.strokeWeight((float) 0.5);
							parent.line(plotX1, y, plotX2, y);
						}
					} else {
						if (view == 5) {
							for (int i = 5000; i <= maxY; i += 5000) {
								float y = PApplet.map(i, 0, maxY,
										plotY2, plotY1);
								parent.text(formatIntegerThousand(i),
										plotX1 - 10, y + 5);
								parent.strokeWeight((float) 0.5);
								parent.line(plotX1, y, plotX2, y);
							}
						} else {
							if (view == 6) {
								for (int i = 5000; i <= maxY; i += 5000) {
									float y = PApplet.map(i, 0, maxY,
											plotY2, plotY1);

									parent.text(formatIntegerThousand(i),
											plotX1 - 10, y + 5);
									parent.strokeWeight((float) 0.5);
									parent.line(plotX1, y, plotX2, y);
								}
							} else {
								if (view == 7) {
									for (int i = 5000; i <= maxY; i += 5000) {
										float y = PApplet.map(i, 0,
												maxY, plotY2, plotY1);
										parent.text(formatIntegerThousand(i),
												plotX1 - 10, y + 5);
										parent.strokeWeight((float) 0.5);
										parent.line(plotX1, y, plotX2, y);

									}
								}
							}
						}
					}

				}
			}
		}
	}

	public String updateNextDay(String prevDay) {
		String s = "";
		if (prevDay == "Sat")
			s = "Sun";
		if (prevDay == "Sun")
			s = "Mon";
		if (prevDay == "Mon")
			s = "Tues";
		if (prevDay == "Tues")
			s = "Wed";
		if (prevDay == "Wed")
			s = "Thurs";
		if (prevDay == "Thurs")
			s = "Fri";
		if (prevDay == "Fri")
			s = "Sat";
		return s;
	}

	public String updatePrevDay(String prevDay) {
		String s = "";
		if (prevDay == "Sat")
			s = "Fri";

		if (prevDay == "Sun")
			s = "Sat";

		if (prevDay == "Mon")
			s = "Sun";

		if (prevDay == "Tues")
			s = "Mon";

		if (prevDay == "Wed")
			s = "Tues";

		if (prevDay == "Thurs")
			s = "Wed";

		if (prevDay == "Fri")
			s = "Thurs";

		return s;
	}

	public void mouseClicked() {
		if (parent.mouseY > tabTop && parent.mouseY < tabBottom
				&& main.MainUFO_Version_2_5.graphButton.s) {
			for (int i = 0; i < 7; i++) {
				if (parent.mouseX > tabLeft[i] && parent.mouseX < tabRight[i]) {
					view = i + 1;
				}
			}
			if (view == 1) {
				for (int i = 0; i < 104; i++) {
					graphValues2[i].set(0);
					graphValues2[i].target(populationSightings[i]);
				}
			}
			if (view == 3) {
				for (int i = 0; i < 50; i++) {
					graphValues[i].set(0);
					// System.out.println(aSightings[i]);
					graphValues[i].target(militarySightings[i]);
					// populationValues[i].update();

				}

			}
			if (view == 2) {
				for (int i = 0; i < 50; i++) {
					graphValues[i].set(0);
					// System.out.println(aSightings[i]);
					graphValues[i].target(airportSightings[i]);
					// populationValues[i].update();

				}
			}
			if (view == 4) {
				for (int i = 0; i < 10; i++) {
					graphValues3[i].set(0);
					// System.out.println(aSightings[i]);
					graphValues3[i].target(yearSightings[i]);
					// populationValues[i].update();

				}

			}
			if (view == 5) {
				for (int i = 0; i < 12; i++) {
					graphValues3[i].set(0);
					// System.out.println(aSightings[i]);
					graphValues3[i].target(monthSightings[i]);
					// populationValues[i].update();

				}

			}
			if (view == 6) {
				for (int i = 0; i < 7; i++) {
					graphValues3[i].set(0);
					graphValues3[i].target(weekSightings[i]);
				}

			}
			if (view == 7) {
				for (int i = 0; i < 24; i++) {
					graphValues3[i].set(0);
					graphValues3[i].target(hourSightings[i]);
				}
			}
		}
	}

	public String formatIntegerThousand(int num) {
		String nStr = "" + num;
		if (num < 1000)
			return "" + num;
		int th = num / 1000;
		return th + "," + nStr.substring(nStr.length() - 3, nStr.length());
	}

}