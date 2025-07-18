import ChatContent from "./ChatContent";
import ChatInfo from "./ChatInfo";
import InputChat from "./InputChat";

const ChatBox = () => {
    return (
        <div className="w-100 h-100 overflow-y-auto hide-scrollbar" style={{backgroundColor: "#ddd"}}>
            {/* Chat info */}
            <ChatInfo />

            {/* Chat content */}
            <ChatContent />

            {/* Input chat */}
            <InputChat />
        </div>
    )
}

export default ChatBox;