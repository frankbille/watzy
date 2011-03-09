package org.apache.wicket.examples.yatzy.frontend.pages;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.examples.yatzy.frontend.YatzyApplication;
import org.apache.wicket.examples.yatzy.frontend.panels.MainActionPanel;
import org.apache.wicket.examples.yatzy.frontend.panels.ScorePanel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.util.tester.ITestPageSource;
import org.apache.wicket.util.tester.WicketTester;
import org.examples.yatzy.MaxiGame;
import org.junit.Test;

public class TestGamePage {

	@Test
	public void testFullGame() {
		WicketTester tester = new WicketTester(new YatzyApplication());
		tester.startPage(new ITestPageSource() {
			private static final long serialVersionUID = 1L;

			public Page getTestPage() {
				return new GamePage(new MaxiGame());
			}
		});
		
		// Add a player
		tester.clickLink("mainActionPanel:content:addNewPlayer");
		tester.setParameterForNextRequest("mainActionPanel:content:nameForm:name", "John Doe");
		tester.clickLink("mainActionPanel:content:nameForm:changeName");
		
		// Start game
		tester.clickLink("mainActionPanel:content:startGame");
		
		/*
		 * Play
		 */
		MainActionPanel mainActionPanel = (MainActionPanel) tester.getComponentFromLastRenderedPage("mainActionPanel");
		while (mainActionPanel.getModelObject().isComplete() == false) {
			// 1. Roll
			tester.clickLink("mainActionPanel:content:rollLink");
			
			// 2. Select available score
			RepeatingView listView = (RepeatingView) tester.getComponentFromLastRenderedPage("scoreCardPanel:scores");
			int scoreCount = listView.size();
			for (int i = 1; i <= scoreCount; i++) {
				String scorePanelPath = "scoreCardPanel:scores:"+i+":scorePanel";
				Component scorePanel = tester.getComponentFromLastRenderedPage(scorePanelPath);
				if (scorePanel instanceof ScorePanel) {
					String combinationLinkPath = scorePanelPath+":combinationLink";
					Component combinationLink = tester.getComponentFromLastRenderedPage(combinationLinkPath);
					if (combinationLink.isEnabled()) {
						tester.clickLink(combinationLinkPath);
						break;
					}
				}
			}
		}
		
		
		tester.debugComponentTrees();
	}
	
}
