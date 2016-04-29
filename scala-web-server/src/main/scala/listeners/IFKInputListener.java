package listeners;

import com.phidgets.event.InputChangeEvent;
import com.phidgets.event.InputChangeListener;

public class IFKInputListener implements InputChangeListener {

	//For smoke detector #420
//	DetecteurIncendie di = null;
	
/*	public IFKInputListener(DetecteurIncendie di){
		this.di = di;
	}
*/
	@Override
	public void inputChanged(InputChangeEvent ice) {
			switch (ice.getIndex())
			{
				case 0 : 
//					di.changeMode();
					break;
				default: 
					break;
			}
	}

}
