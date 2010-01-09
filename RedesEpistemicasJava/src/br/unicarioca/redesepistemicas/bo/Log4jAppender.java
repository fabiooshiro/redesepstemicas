package br.unicarioca.redesepistemicas.bo;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

import br.unicarioca.redesepistemicas.view.MainFrame;

public class Log4jAppender extends AppenderSkeleton{

	@Override
	protected void append(LoggingEvent event) {
		String message = "";
		if( this.layout == null ){
			message = "Erro: Sem layout de mensagem";
		}else{
			message = this.layout.format(event);
		}
		//Enviar a mensagem para o MainFrame
		MainFrame mf = MainFrame.getLastInstance();
		if(mf!=null){
			mf.info(message);
		}
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean requiresLayout() {
		return true;
	}

}
