package org.examples.yatzy;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.examples.yatzy.score.IScore;
import org.examples.yatzy.score.IScoreCard;
import org.examples.yatzy.score.IScoreGroup;
import org.examples.yatzy.score.ITurnScore;
import org.examples.yatzy.score.SumScore;

public class GamePrinter {

	public static void printGame(IGame game) {
		System.out.println(dumpGame(game));
	}

	public static CharSequence dumpGame(IGame game) {
		StringBuilder b = new StringBuilder();

		b.append(game.getClass().getSimpleName()).append("\n");
		b.append(dumpScoreCard(game.getScoreCard()));

		return b;
	}

	public static CharSequence dumpScoreCard(IScoreCard scoreCard) {
		// Headers
		List<String> headers = new ArrayList<String>();
		headers.add("");
		for (IPlayer player : scoreCard.getPlayers()) {
			headers.add(player.getName());
		}

		// Data
		final List<List<String>> data = new ArrayList<List<String>>();
		traverseScore(scoreCard, new IScoreVisitor() {
			public void visit(IScore score) {
				if (score instanceof ITurnScore) {
					ITurnScore turnScore = (ITurnScore) score;
					List<String> row = new ArrayList<String>();

					String scoreName = turnScore.getClass().getSimpleName();
					if (turnScore instanceof SumScore) {
						SumScore sumScore = (SumScore) turnScore;
						scoreName += " " + sumScore.getValue();
					}
					row.add(scoreName);

					for (IPlayer player : turnScore.getPlayers()) {
						row.add("" + turnScore.getScore(player));
					}

					data.add(row);
				} else if (score instanceof IScoreGroup) {
					IScoreGroup scoreGroup = (IScoreGroup) score;

					List<String> row = new ArrayList<String>();

					row.add("Total");
					for (IPlayer player : scoreGroup.getPlayers()) {
						row.add("" + scoreGroup.getScore(player));
					}

					data.add(row);
				}
			}
		});

		return dumpTable(headers, data);
	}

	public static CharSequence dumpTable(List<String> headers, List<List<String>> data) {
		StringBuilder table = new StringBuilder();

		// Determine column widths
		List<Integer> columnWidths = new ArrayList<Integer>();
		determineColumnWidths(columnWidths, headers);
		for (List<String> row : data) {
			determineColumnWidths(columnWidths, row);
		}

		// Print top line
		table.append(dumpLine(columnWidths)).append("\n");

		// Print headers
		table.append(dumpRow(headers, columnWidths)).append("\n");

		table.append(dumpLine(columnWidths)).append("\n");

		// Print rows
		for (List<String> row : data) {
			table.append(dumpRow(row, columnWidths)).append("\n");

			// Print bottom line
			table.append(dumpLine(columnWidths)).append("\n");
		}

		return table;
	}

	private static void determineColumnWidths(List<Integer> columnWidths, List<String> row) {
		SortedMap<Integer, Integer> cws = new TreeMap<Integer, Integer>();
		int columnNo = 0;
		for (Integer width : columnWidths) {
			cws.put(columnNo, width);
			columnNo++;
		}

		columnNo = 0;
		for (String cell : row) {
			int width = get(cell).length();

			int origWidth = 0;
			if (cws.containsKey(columnNo)) {
				origWidth = cws.get(columnNo);
			}

			if (width >= origWidth) {
				cws.put(columnNo, width);
			}

			columnNo++;
		}

		columnWidths.clear();
		columnWidths.addAll(cws.values());
	}

	private static CharSequence dumpLine(List<Integer> columnWidths) {
		StringBuilder line = new StringBuilder();

		line.append("+");
		for (Integer width : columnWidths) {
			line.append(repeat("-", width + 2));
			line.append("+");
		}

		return line;
	}

	private static CharSequence dumpRow(List<String> row, List<Integer> columnWidths) {
		StringBuilder r = new StringBuilder();

		r.append("| ");
		int columnNo = 0;
		for (String cell : row) {
			Integer width = columnWidths.get(columnNo);

			r.append(fill(cell, width));
			r.append(" | ");

			columnNo++;
		}

		return r;
	}

	private static CharSequence fill(CharSequence value, int width) {
		return fill(value, width, " ");
	}

	private static CharSequence fill(CharSequence value, int width, String filler) {
		StringBuilder f = new StringBuilder();

		f.append(value);
		f.append(repeat(filler, width - value.length()));

		return f;
	}

	private static String get(String value) {
		return value != null ? value : "";
	}

	private static CharSequence repeat(CharSequence string, int count) {
		StringBuilder b = new StringBuilder();

		for (int i = 0; i < count; i++) {
			b.append(string);
		}

		return b;
	}

	private static interface IScoreVisitor {
		void visit(IScore score);
	}

	private static void traverseScore(IScore score, IScoreVisitor scoreVisitor) {
		scoreVisitor.visit(score);

		if (score instanceof IScoreGroup) {
			IScoreGroup scoreGroup = (IScoreGroup) score;
			for (IScore childScore : scoreGroup.getScores()) {
				traverseScore(childScore, scoreVisitor);
			}
		}
	}

	public static CharSequence dumpTurn(ITurn turn) {
		StringBuilder b = new StringBuilder();

		b.append("P: ").append(turn.getPlayer().getName()).append(" - D: ");
		List<IDice> diceList = turn.getDiceList();
		for (IDice dice : diceList) {
			b.append(dice.getValue());
			if (turn.shouldHold(dice)) {
				b.append("*");
			}
			b.append(" ");
		}
		b.append(turn.mayRoll());

		return b;
	}

	public static CharSequence dumpScore(IScore score, IPlayer player) {
		StringBuilder s = new StringBuilder();

		s.append(score.getClass().getSimpleName());
		if (score instanceof SumScore) {
			SumScore sumScore = (SumScore) score;
			s.append(" (");
			s.append(sumScore.getValue());
			s.append(")");
		}

		if (player != null) {
			if (score.hasScore(player)) {
				s.append(" = ");
				s.append(score.getScore(player));
			}
		}

		return s;
	}

}
