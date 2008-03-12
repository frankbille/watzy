package org.apache.wicket.examples.yatzy.domain;

import java.io.Serializable;
import java.util.List;

public interface IDiceFactory extends Serializable {

	List<IDice> createDiceList();

}
