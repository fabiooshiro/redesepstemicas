package br.unicarioca.redesepistemicas.bo;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

import br.unicarioca.redesepistemicas.view.MainFrame;

/**
 * Responsavel por desviar os INFO para a interface do usuario
 */
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
		InfoListener mf = MainFrame.getLastInstance();
		if(mf!=null){
			mf.info(message);
		}
	}

	@Override
	public void close() {
		
	}

	@Override
	public boolean requiresLayout() {
		return true;
	}

}
