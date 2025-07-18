import { Outlet } from "react-router-dom"
import Sidebar from "../components/Sidebar/Sidebar"
import ChatBox from "../components/Chatbox/ChatBox"

const MasterLayout = () => {
    return (
        <div className='main-layout h-100vh'>
            <div className="d-flex h-100">
                <Sidebar />
                <div className="bg-white min-w-300px max-w-300px h-100vh hide-scrollbar" style={{overflowY: 'auto'}}>
                    <Outlet />
                </div>
                <ChatBox />
            </div>
        </div>
    )
}

export { MasterLayout }