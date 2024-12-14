import Link from "next/link";

const Drawer = () => {
    return (
        <div className="drawer-side top-16 z-20 h-[calc(100vh-4rem)] ">
            <label htmlFor="my-drawer" aria-label="close sidebar" className="drawer-overlay"></label>
            <ul className="menu bg-base-200 text-base-content min-h-full w-[20%] p-4">
                {/* Sidebar content here */}
                <Link href="/employees">Sidebar Item 1</Link>
                <Link href="">Sidebar Item 2</Link>
            </ul>
        </div>
    )
}

export default Drawer;