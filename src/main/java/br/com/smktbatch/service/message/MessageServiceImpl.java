package br.com.smktbatch.service.message;

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import br.com.smktbatch.repository.remote.MessageRepository;

@Service
public class MessageServiceImpl implements MessageService {

	private final MessageRepository messageRepository;

	private static final Logger LOG = getLogger(MessageServiceImpl.class);

	MessageServiceImpl(MessageRepository messageRepository) {
		this.messageRepository = messageRepository;
	}

	@Override
	public String getByCode(String code) {
		LOG.info("getByCode()");
		return this.messageRepository.findByCode(code).getDescription();
	}

}
