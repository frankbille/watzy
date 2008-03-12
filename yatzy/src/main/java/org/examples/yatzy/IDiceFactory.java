package org.examples.yatzy;

import java.io.Serializable;
import java.util.List;

public interface IDiceFactory extends Serializable {

	List<IDice> createDiceList();

}
