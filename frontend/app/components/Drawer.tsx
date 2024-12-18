import Link from "next/link";

type DrawerProps = {
    closeDrawer: () => void;
  };

const Drawer = ({ closeDrawer }: DrawerProps) => {
    return (
        <div className="drawer-side top-16 z-20 h-[calc(100vh-4rem)] ">
            <label htmlFor="my-drawer" aria-label="close sidebar" className="drawer-overlay" onClick={closeDrawer}></label>
            <ul className="menu bg-base-200 text-base-content min-h-full w-[20%] p-4">
                {/* Sidebar content here */}
                <Link href="/employees" onClick={closeDrawer}>Profile</Link>
                <Link href="" onClick={closeDrawer}>Sidebar Item 2</Link>
            </ul>
        </div>
    )
}

export default Drawer;