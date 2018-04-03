package com.matthew0x40.yahtzee.models.scoreboard;

import java.util.ArrayList;
import java.util.List;

import com.matthew0x40.yahtzee.models.Hand;
import com.matthew0x40.yahtzee.models.Settings;

/**
 * The root Scoreboard.
 */
public class RootScoreboard extends Scoreboard {

	public RootScoreboard(Settings s) {
		super(s);
	}
	
	@Override
	public boolean isComplete() {
		//System.out.println("CHECKING IF COMPLETE");
		boolean complete = true;
		for (Scoreboard sb : this.children) {
			if (!sb.isComplete()) {
				complete = false;
				break;
			}
		}
		return complete;
	}

	@Override
	public int getTotal() {
		return this.children.stream().mapToInt(board -> board.getTotal()).sum();
	}

	@Override
	public List<ScoringOption> receive(Hand hand, List<ScoringOption> ret) {
		this.children.stream().forEach(sb -> ret.addAll(sb.receive(hand)));
		return ret;
	}

	@Override
	public List<ScoringOption> toScoreList() {
		List<ScoringOption> sl = new ArrayList<>();
		this.children.forEach(sb -> sl.addAll(sb.toScoreList()));
		return sl;
	}
	
}
