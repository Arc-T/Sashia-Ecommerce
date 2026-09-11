import { useTranslation } from "react-i18next";
import PageHeader from "@/components/custom/page-header";
import DataTable from "@/components/custom/datatable/table";
import { useState } from "react";
import ActionBar from "@/components/custom/datatable/search-bar";
import AttributeSearch from "./-search";
import type { CategoryFilter } from "@/types/category";
import { useGetCategories } from "@/hooks/use-categories";
import { categoryListColumn } from "./-column";
import { List } from "lucide-react";

export function CategoriesList() {
  const { t } = useTranslation();
  const [filter, setFilter] = useState<Partial<CategoryFilter>>({});
  const { isLoading, data: attributes } = useGetCategories(filter);

  return (
    <>
      <PageHeader title={t("categories_list")} icon={<List />} />
      <div className="space-y-2">
        <div className="flex items-center justify-start gap-4">
          <div className="flex-1">
            <ActionBar addLink={""}>
              <AttributeSearch
                onSearch={(params) => setFilter(params)}
                onClear={() => setFilter({})}
              />
            </ActionBar>
          </div>
        </div>

        <DataTable
          columns={categoryListColumn}
          data={attributes}
          isLoading={isLoading}
        />
      </div>
    </>
  );
}
