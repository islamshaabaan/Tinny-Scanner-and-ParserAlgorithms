var myObj={"0":{"readStatement":"x"},"1":{"readStatement":"choice"},"2":{"ifStatement":{"testPart":{"operation":{"op":"=","leftOperand":{"identifier":"choice"},"rightOperand":{"number":"0"}}},"elsePart":{"4":{"assign":{"rightOperand":{"number":"1"},"IdentifierName":"result"}}},"thenPart":{"3":{"assign":{"rightOperand":{"number":"0"},"IdentifierName":"result"}}}}},"5":{"repeatStatement":{"repeatTest":{"operation":{"op":"=","leftOperand":{"identifier":"x"},"rightOperand":{"number":"0"}}},"repeatBody":{"6":{"ifStatement":{"testPart":{"operation":{"op":"=","leftOperand":{"identifier":"choice"},"rightOperand":{"number":"0"}}},"elsePart":{"8":{"assign":{"rightOperand":{"operation":{"op":"*","leftOperand":{"identifier":"result"},"rightOperand":{"identifier":"x"}}},"IdentifierName":"result"}}},"thenPart":{"7":{"assign":{"rightOperand":{"operation":{"op":"+","leftOperand":{"identifier":"result"},"rightOperand":{"identifier":"x"}}},"IdentifierName":"result"}}}}},"9":{"assign":{"rightOperand":{"operation":{"op":"-","leftOperand":{"identifier":"x"},"rightOperand":{"number":"1"}}},"IdentifierName":"x"}}}}},"10":{"writeStatement":{"identifier":"result"}}}