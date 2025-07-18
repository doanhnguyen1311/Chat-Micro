import { Ellipsis, Smile, Mic, SendHorizontal } from "lucide-react";

const InputChat = () => {
    return (
        <div className="d-flex justify-between align-center bg-chat p-24 gap-28px sticky bottom-0">
            <div className="d-flex justify-between align-center gap-28px text-color-secondary cursor-pointer">
                <Ellipsis size={20} className="active" />
                <Smile size={20} className="active" />
            </div>
            <div className="w-100">
                <input 
                    type="text" 
                    placeholder="Type your message..." 
                    className="bg-white radius-4 py-12 px-16"
                    style={{width: 'calc(100% - 32px)'}}
                />
            </div>
            <div className="d-flex justify-between align-center gap-28px text-color-secondary cursor-pointer">
                <Mic size={20} className="active" />
                <SendHorizontal size={20} className="btn-send" />
            </div>
        </div>
    )
}

export default InputChat;