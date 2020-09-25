package br.com.smktbatch.service.message;

import org.springframework.stereotype.Service;

import br.com.smktbatch.repository.remote.MessageRepository;

@Service
public class MessageServiceImpl implements MessageService {

	private final MessageRepository messageRepository;

	MessageServiceImpl(MessageRepository messageRepository) {
		this.messageRepository = messageRepository;
	}

	@Override
	public String getByCode(String code) {
		return this.messageRepository.findByCode(code).getDescription();
	}

}
