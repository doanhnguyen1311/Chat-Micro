import { Outlet } from "react-router-dom"
import Sidebar from "../components/Sidebar"
import MessengerSidebar from "../components/Messenger"
import Header from "../components/Header"
import RightSidebar from "../components/RightSidebar"

const MasterLayout = () => {
    return (
        <div className='main-layout h-100vh'>
            <div className="d-flex h-100">
                <Sidebar/>
                <div className="bg-white h-100vh w-100 hide-scrollbar" style={{overflowY: 'auto'}}>
                    <Header />
                    <div className="d-flex">
                        <Outlet />
                        <RightSidebar />
                    </div>
                </div>
                <MessengerSidebar />
            </div>
        </div>
    )
}

export { MasterLayout }