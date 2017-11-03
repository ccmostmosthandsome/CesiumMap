package com.niuzhendong.websocket.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;


/** 
 * websocket ������ 
 *  
 * @author tomZ 
 * @date 2016��11��2�� 
 * @desc TODO 
 */  
@Component  
public class GpsWebSocketHandler implements WebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(GpsWebSocketHandler.class);  
    
    //�������е��û�session
    private static final Map<String, WebSocketSession> sessions = new HashMap<String, WebSocketSession>(); 
	
    
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus arg1) throws Exception {
		// TODO Auto-generated method stub
		logger.info("connection closed . sessionId {}, remote addr {}", session.getId(), session.getRemoteAddress());  
        sessions.remove(session.getId());
	}

	//���� ����ʱ 
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		// TODO Auto-generated method stub
		logger.info("connection established . sessionId {}, remote addr {}", session.getId(),  
                session.getRemoteAddress());  
        sessions.put(session.getId(), session);
	}

	//������Ϣ
	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> msg) throws Exception {
		// TODO Auto-generated method stub
		logger.info("handler message. sessionId {}, remote addr {}, message {}", session.getId(),  
                session.getRemoteAddress());  
        sendMessage(msg);  
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable e) throws Exception {
		// TODO Auto-generated method stub
		 if (session.isOpen()) {  
	            sessions.remove(session.getId());  
	            session.close();  
	        }  
	        logger.error("handle trainsport error", e);  
	}

	@Override
	public boolean supportsPartialMessages() {
		// TODO Auto-generated method stub
		return false;
	}

	 /** 
     * ������Ϣ 
     *  
     * @param msg 
     */  
    public void sendMessage(WebSocketMessage<?> msg) {  
        for (Entry<String, WebSocketSession> ses : sessions.entrySet()) {  
            WebSocketSession s = ses.getValue();  
            try {  
                if (s.isOpen()) {  
                    s.sendMessage(msg);  
                } else {  
                    sessions.remove(s.getId());  
                }  
            } catch (IOException e) {  
                logger.error(e.getMessage(), e);  
            }  
        }  
    }
    
    /** 
     * ������Ϣ 
     *  
     * @param msg 
     */  
    public void sendMessage(TextMessage msg) {  
        for (Entry<String, WebSocketSession> ses : sessions.entrySet()) {  
            WebSocketSession s = ses.getValue();  
            try {  
                if (s.isOpen()) {  
                    s.sendMessage(msg);  
                } else {  
                    sessions.remove(s.getId());  
                }  
            } catch (IOException e) {  
                logger.error(e.getMessage(), e);  
            }  
        }  
    }
}
